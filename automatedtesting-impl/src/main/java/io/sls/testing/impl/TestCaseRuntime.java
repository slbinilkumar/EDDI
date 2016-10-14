package io.sls.testing.impl;

import io.sls.core.rest.IRestBotAdministration;
import io.sls.core.rest.IRestBotEngine;
import io.sls.core.service.restinterfaces.IRestInterfaceFactory;
import io.sls.memory.IConversationMemoryStore;
import io.sls.memory.model.ConversationMemorySnapshot;
import io.sls.memory.model.ConversationState;
import io.sls.memory.model.Deployment;
import io.sls.runtime.SystemRuntime;
import io.sls.runtime.ThreadContext;
import io.sls.testing.ITestCaseStore;
import io.sls.testing.model.TestCase;
import io.sls.testing.model.TestCaseState;
import io.sls.utilities.RuntimeUtilities;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Copyright by Spoken Language System. All rights reserved.
 * User: jarisch
 * Date: 22.11.12
 * Time: 17:01
 */
public class TestCaseRuntime {
    private final IRestInterfaceFactory restInterfaceFactory;
    private final String coreServerURI;
    private final ITestCaseStore testCaseStore;
    private final IConversationMemoryStore conversationMemoryStore;

    @Inject
    public TestCaseRuntime(IRestInterfaceFactory restInterfaceFactory,
                           @Named("coreServerURI") String coreServerURI,
                           ITestCaseStore testCaseStore, IConversationMemoryStore conversationMemoryStore) {
        this.restInterfaceFactory = restInterfaceFactory;
        this.coreServerURI = coreServerURI;
        this.testCaseStore = testCaseStore;
        this.conversationMemoryStore = conversationMemoryStore;
    }

    public void executeTestCase(final String id, final TestCase testCase) throws Exception {
        SystemRuntime.getRuntime().submitCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    testCaseStore.setTestCaseState(id, TestCaseState.IN_PROGRESS);

                    if (!isBotDeployed(testCase.getBotId(), testCase.getBotVersion())) {
                        deployBot(testCase.getBotId(), testCase.getBotVersion());
                    }

                    ConversationMemorySnapshot actual = runTestCase(testCase.getBotId(), testCase);
                    testCase.setActual(actual);
                    testCase.setLastRun(new Date(System.currentTimeMillis()));
                    testCase.setTestCaseState(testCase.getExpected().equals(testCase.getActual()) ? TestCaseState.SUCCESS : TestCaseState.FAILED);
                    testCaseStore.storeTestCase(id, testCase);
                } catch (Exception e) {
                    testCaseStore.setTestCaseState(id, TestCaseState.ERROR);
                    throw e;
                }
                return null;
            }
        }, ThreadContext.getResources());
    }

    private boolean isBotDeployed(String botId, Integer botVersion) throws Exception {
        IRestBotAdministration restBotAdministration = restInterfaceFactory.get(IRestBotAdministration.class, coreServerURI);
        String deploymentStatus;
        do {
            deploymentStatus = restBotAdministration.getDeploymentStatus(Deployment.Environment.test, botId, botVersion);
            if (Objects.equals(deploymentStatus, Deployment.Status.IN_PROGRESS.toString())) {
                Thread.sleep(1000);
            } else {
                break;
            }
        } while (true);

        return !Objects.equals(deploymentStatus, Deployment.Status.NOT_FOUND.toString());
    }

    private void deployBot(String botId, Integer botVersion) throws Exception {
        IRestBotAdministration restBotAdministration = restInterfaceFactory.get(IRestBotAdministration.class, coreServerURI);
        restBotAdministration.deployBot(Deployment.Environment.test, botId, botVersion);
        while (true) {
            //wait until deployment has finished
            if (!Objects.equals(restBotAdministration.getDeploymentStatus(Deployment.Environment.test, botId, botVersion), Deployment.Status.IN_PROGRESS.toString())) {
                break;
            } else {
                Thread.sleep(1000);
            }
        }
    }

    private ConversationMemorySnapshot runTestCase(String botId, TestCase testCase) throws Exception {
        IRestBotEngine botEngine = restInterfaceFactory.get(IRestBotEngine.class, coreServerURI);

        Response ConversationResponse = botEngine.startConversation(Deployment.Environment.test, botId);
        URI conversationURI =   ConversationResponse.getLocation();
        String conversationURIPath = conversationURI.getPath();
        String conversationId = conversationURIPath.substring(conversationURIPath.lastIndexOf("/") + 1);
        ConversationMemorySnapshot expected = testCase.getExpected();
        List<ConversationMemorySnapshot.ConversationStepSnapshot> expectedConversationSteps = expected.getConversationSteps();
        //we skip the first one, since the initial run has already been done at this point (at startConversation)
        for (int i = 1; i < expectedConversationSteps.size(); i++) {
            ConversationMemorySnapshot.ConversationStepSnapshot expectedConversationStep = expectedConversationSteps.get(i);
            String input = getFirstInput(expectedConversationStep);
            if (RuntimeUtilities.isNullOrEmpty(input)) {
                input = " ";
            }
            botEngine.say(Deployment.Environment.test, botId, conversationId, input);
            while (botEngine.getConversationState(Deployment.Environment.test, conversationId) == ConversationState.IN_PROGRESS) {
                Thread.sleep(1000);
            }
        }

        return conversationMemoryStore.loadConversationMemorySnapshot(conversationId);
    }

    private String getFirstInput(ConversationMemorySnapshot.ConversationStepSnapshot conversationStep) {
        for (ConversationMemorySnapshot.PackageRunSnapshot packageRunSnapshot : conversationStep.getPackages()) {
            for (ConversationMemorySnapshot.ResultSnapshot resultSnapshot : packageRunSnapshot.getLifecycleTasks()) {
                if (resultSnapshot.getKey().startsWith("input")) {
                    return resultSnapshot.getResult().toString();
                }
            }
        }

        return null;
    }
}


package io.sls.core.bootstrap;

import com.google.inject.Scopes;
import com.google.inject.multibindings.MapBinder;
import io.sls.botmarklet.rest.IRestBotmarklet;
import io.sls.botmarklet.rest.impl.RestBotmarklet;
import io.sls.core.behavior.BehaviorRulesEvaluationTask;
import io.sls.core.lifecycle.ILifecycleTask;
import io.sls.core.media.MediaTask;
import io.sls.core.normalizing.NormalizeInputTask;
import io.sls.core.output.SimpleOutputTask;
import io.sls.core.parser.InputParserTask;
import io.sls.core.rest.IRestBotAdministration;
import io.sls.core.rest.IRestBotEngine;
import io.sls.core.rest.IRestBotUI;
import io.sls.core.runtime.CoreRuntime;
import io.sls.core.runtime.IBotFactory;
import io.sls.core.runtime.IPackageFactory;
import io.sls.core.runtime.client.configuration.IResourceClientLibrary;
import io.sls.core.runtime.client.configuration.ResourceClientLibrary;
import io.sls.core.runtime.client.packages.IPackageStoreClientLibrary;
import io.sls.core.runtime.client.packages.PackageStoreClientLibrary;
import io.sls.core.runtime.internal.BotFactory;
import io.sls.core.runtime.internal.PackageFactory;
import io.sls.core.runtime.service.BotStoreService;
import io.sls.core.runtime.service.IBotStoreService;
import io.sls.core.runtime.service.IPackageStoreService;
import io.sls.core.runtime.service.PackageStoreService;
import io.sls.core.sendmail.SendMailTask;
import io.sls.core.tts.TextToSpeechTask;
import io.sls.core.ui.rest.internal.RestBotAdministration;
import io.sls.core.ui.rest.internal.RestBotEngine;
import io.sls.core.ui.rest.internal.RestBotUI;
import io.sls.faces.html.IHtmlFaceStore;
import io.sls.faces.html.impl.HtmlFaceStore;
import io.sls.logging.client.rest.IRestClientLogging;
import io.sls.logging.client.rest.impl.RestClientLogging;
import io.sls.runtime.bootstrap.AbstractBaseModule;

import java.io.InputStream;

public class CoreModule extends AbstractBaseModule {
    public CoreModule(InputStream... inputStream) {
        super(inputStream);
    }

    @Override
    protected void configure() {
        registerConfigFiles(configFiles);

        bind(IResourceClientLibrary.class).to(ResourceClientLibrary.class).in(Scopes.SINGLETON);
        bind(IPackageStoreClientLibrary.class).to(PackageStoreClientLibrary.class).in(Scopes.SINGLETON);
        bind(IPackageStoreService.class).to(PackageStoreService.class).in(Scopes.SINGLETON);
        bind(IBotStoreService.class).to(BotStoreService.class).in(Scopes.SINGLETON);

        bind(IBotFactory.class).to(BotFactory.class).in(Scopes.SINGLETON);
        bind(IPackageFactory.class).to(PackageFactory.class).in(Scopes.SINGLETON);

        bind(IHtmlFaceStore.class).to(HtmlFaceStore.class).in(Scopes.SINGLETON);

        bind(IRestBotEngine.class).to(RestBotEngine.class);
        bind(IRestBotAdministration.class).to(RestBotAdministration.class);
        bind(IRestBotUI.class).to(RestBotUI.class);
        bind(IRestBotmarklet.class).to(RestBotmarklet.class);
        bind(IRestClientLogging.class).to(RestClientLogging.class);

        MapBinder<String, ILifecycleTask> lifecycleTaskPlugins
                = MapBinder.newMapBinder(binder(), String.class, ILifecycleTask.class);
        lifecycleTaskPlugins.addBinding("io.sls.normalizer").to(NormalizeInputTask.class);
        lifecycleTaskPlugins.addBinding("io.sls.parser").to(InputParserTask.class);
        lifecycleTaskPlugins.addBinding("io.sls.behavior").to(BehaviorRulesEvaluationTask.class);
        lifecycleTaskPlugins.addBinding("io.sls.output").to(SimpleOutputTask.class);
        lifecycleTaskPlugins.addBinding("io.sls.media").to(MediaTask.class);
        lifecycleTaskPlugins.addBinding("io.sls.tts").to(TextToSpeechTask.class);
        lifecycleTaskPlugins.addBinding("io.sls.sendmail").to(SendMailTask.class);


        bind(CoreRuntime.class).asEagerSingleton();
    }
}

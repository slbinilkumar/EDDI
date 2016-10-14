var botId;
var conversationId;
var environment;
var baseURL = window.location.protocol + '//' + window.location.host;
var basePath;

$(document).ready(function () {
    try {
        initKeyEvent();
        botId = $('#botId').val();
        environment = $('#environment').val();
        startNewConversation();

        $('#user_input_button_submit').click(function () {
            submitInput();
        });

        $('#undo').click(function () {
            undo();
        });

        $('#redo').click(function () {
            redo();
        });

        $('#reinit').click(function () {
            startNewConversation();
        });

        basePath = window.location.pathname;
        basePath = basePath.substring(0, basePath.lastIndexOf('/') + 1);
    } catch (e) {
        log('ERROR', e.message);
    }
});

function undo() {
    IRestBotEngine.undo({environment:environment, botId:botId, conversationId:conversationId});
    refreshConversationLog();
}

function redo() {
    IRestBotEngine.redo({environment:environment, botId:botId, conversationId:conversationId});
    refreshConversationLog();
}

function submitInput() {
    var message = $('#input_user').val();
    if (message.length > 0) {
        $('#user_input_button_submit').addClass('ui-disabled');
        addToLog(message, 'user');
        $('#input_user').val('');
        IRestBotEngine.say({environment:environment, botId:botId, conversationId:conversationId, $entity:message});
        refreshConversationLog();
    }
}

var refreshConversationLog = function () {
    $('#status_indicator').css('visibility', 'visible');
    var conversationMemory = IRestBotEngine.readConversationLog({environment:environment, botId:botId, conversationId:conversationId});

    if (conversationMemory === 'undefined' || conversationMemory === '') {
        startNewConversation();
    }

    var conversationState = conversationMemory.conversationState;

    if (conversationState == 'ERROR') {
        $('#status_indicator').css('visibility', 'hidden');
        $('#user_input_button_submit').removeClass('ui-disabled');
        alert("An Error has occurred. Please contact the administrator!");
        return;
    }

    if (conversationState == 'ENDED') {

    }

    if (conversationState == 'IN_PROGRESS') {
        setTimeout(refreshConversationLog, 1000);
        return;
    }

    $('#user_input_button_submit').removeClass('ui-disabled');

    var ioList = [];
    for (var i = 0; i < conversationMemory.conversationSteps.length; i++) {
        var step = conversationMemory.conversationSteps[i];

        var input = null;
        var output = null;
        var media = null;
        for (var x = 0; x < step.data.length; x++) {
            var obj = step.data[x];
            if (obj.key.indexOf('input') == 0) {
                input = obj.value;
            } else if (obj.key.indexOf('output') == 0) {
                output = obj.value;
            } else if (obj.key.indexOf('media') == 0) {
                media = obj.value;
            } else if (obj.key.indexOf('actions') == 0) {
            }
        }

        ioList.push({input:input, output:output, media:media});
    }

    if (ioList.length > 0) {
        var latestInteraction = ioList[ioList.length - 1];
        if (latestInteraction === 'undefined') {
            latestInteraction = {};
        }

        //apply media/images
        //var mediaURI = latestInteraction.media;
        /*if (typeof mediaURI !== 'undefined' && mediaURI.indexOf('image') == 0) {
         var mediaURL = baseURL + '/binary/default/mobile/images' + mediaURI.substring(mediaURI.lastIndexOf('/'));
         var img = $('<img id="' + mediaURI.substring(mediaURI.lastIndexOf('/' + 1, mediaURI.lastIndexOf('.'))) + '">');
         img.attr('src', mediaURL);
         img.attr('class', 'media_img');
         $('#desktop').html(img);
         } else {
         $('#desktop').html('<div style="text-align: center;width: 100%; height: 100%;">Nothing to show at the moment..</div>');
         }*/

        if (latestInteraction.output == null) {
            latestInteraction.output = '';
        }

        var mediaURIsString = latestInteraction.media;
        if (mediaURIsString != null) {
            var mediaURIs = mediaURIsString.split(',');
            for (var n = 0; n < mediaURIs.length; n++) {
                var mediaURI = mediaURIs[n];
                if (mediaURI.indexOf('pdf') == 0) {
                    var fileName = mediaURI.substring(mediaURI.lastIndexOf('/') + 1);
                    var mediaURL = baseURL + '/binary/default/desktop/binary/' + fileName;
                    latestInteraction.output += '<br /><a target="_blank" href="' + mediaURL + '">' + fileName + '</a>'
                }
            }
        }

        addToLog(latestInteraction.output, 'eddi');
    }

    if (conversationState != 'ENDED' && ioList.length > 1) {
        $('#undo').show();
    } else {
        $('#undo').hide();
    }

    if (conversationState != 'ENDED' && conversationMemory.redoCacheSize > 0) {
        $('#redo').show();
    } else {
        $('#redo').hide();
    }

    $('#status_indicator').css('visibility', 'hidden');
}

function addToLog(logEntry, sender) {
    var outerDiv = jQuery('<div/>');
    outerDiv.attr('class', 'log_entry');
    var innerDiv = jQuery('<div/>');
    innerDiv.attr('class', sender);
    innerDiv.append(logEntry);
    outerDiv.append(innerDiv);
    $('#log').prepend(outerDiv);
}

function clearLog() {
    $('#log').empty();
}

function startNewConversation() {
    var uri = IRestBotEngine.startConversation({environment:environment, botId:botId});
    if (typeof uri === 'undefined') {
        alert('Cannot start new conversation with bot! No bot deployed? [botId=' + botId + ']')
    } else {
        clearLog();
        var conversationUriArray = uri.split("/");
        conversationId = conversationUriArray[conversationUriArray.length - 1];
        refreshConversationLog();
    }
}

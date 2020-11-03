function init() {
    registerHandler();
};

function registerHandler() {
    var eventBus = new EventBus('http://localhost:8082/locations');
    eventBus.onopen = function () {
        eventBus.registerHandler('newLocation', function (error, message) {
            document.getElementById('locations').value +=message.body+'\n\n----------------\n\n';
        });
    }
};
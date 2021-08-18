
var ws;
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    ws = new WebSocket('ws://localhost:8080/name');
    ws.onmessage = function (data) {
        showGreeting(data.data);
    }
    setConnected(true);
}

function disconnect() {
    if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var data = JSON.stringify({'name': $("#name").val()})
    ws.send(data);
}

function showGreeting(message) {
    jsonLog = JSON.parse(message);
    id = jsonLog.id;
    logTimestamp = jsonLog.logTimestamp;
    var date = new Date(logTimestamp);
    messageContent = jsonLog.messageContent;
    errorMessage = jsonLog.errorMessage;
    authError = jsonLog.authError;
    exceptionMessage = jsonLog.exceptionMessage;
    httpStatus = jsonLog.httpStatus;

    var s = "<td>" + id + "</td>"
            + "<td>" + httpStatus + "</td>"
            + "<td>" + date + "</td>"
            + "<td>" + ((messageContent == null) ? '' : ('messageContent ' + JSON.stringify(messageContent))) + "</td>"
            + "<td>" + ((errorMessage == null) ? '' : ('errorMessage ' + JSON.stringify(errorMessage)))
            + ((authError == null) ? '' : ('authError ' + JSON.stringify(authError)))
            + ((exceptionMessage == null) ? '' : ('<BR>exceptionMessage ' + JSON.stringify(exceptionMessage)))
            + "</td>";

    // messageContent":{"source":"APM-Checkmk","equipmentClass":"u_server_cluster","equipmentReference":"openshift12345","statusInformation":[{"statusName":"AV Program Status","statusValue":"FAULTY","statusAdditionalInfo":""}],"statusTime":"2020-03-26T10:15:40.857Z"},"httpStatus":"OK","authError":null,"errorMessage":null,"exceptionMessage":null}

    $("#greetings").append("<tr>" + s + "</tr>");
}

function sendTestData() {
    // construct an HTTP request
    var xhr = new XMLHttpRequest();
    xhr.open('POST', './serviceHealth', true);
    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    xhr.setRequestHeader('Accept', 'application/json; charset=UTF-8');
    // send the collected data as JSON
    let data = '[{"source":"APM-Checkmk","equipmentClass":"u_server_cluster","equipmentReference":"openshift12345","statusTime":"2020-03-26T10:15:40.857Z","statusInformation":[{"statusName":"AV Program Status", "statusValue" : "FAULTY", "statusAdditionalInfo":""} ]}]'
    document.getElementById('send').innerHTML = 'POSTing ' + data;
    xhr.send(data);
    xhr.onloadend = function () {
        document.getElementById('receive').innerHTML = 'Response ' + xhr.responseText;
    };
}


$(document).ready(function () {
    connect();

    $("#form1").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
        document.getElementById('messageholder').innerHTML = '';
    });
    $("#disconnect").click(function () {
        disconnect();
        document.getElementById('messageholder').innerHTML = '';
    });
    
    $("#sendtestdata").click(function () {
        sendTestData()
    });

    $("#send").click(function () {
        sendName();
    });
});



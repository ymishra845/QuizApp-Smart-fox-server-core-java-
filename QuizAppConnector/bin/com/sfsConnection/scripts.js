// Initialize sfs variable
var sfs;

var clientGameStateKey=-1;



// Define your own constant for the request ID
const MULTIHANDLER_REQUEST_ID = "requestID";

function sendPing() {
    if (sfs) {
        var params = new SFS2X.SFSObject();
        sfs.send(new SFS2X.ExtensionRequest("game.keepAlive", params));
        console.log("Ping sent to keep connection alive.");
    } else {
        console.log("SmartFoxServer connection not established.");
    }
}

// Add a setInterval function to periodically send the "ping" message
function startKeepAlive() {
    setInterval(sendPing, 6000); // Adjust the interval as needed (e.g., every 60 seconds)
}

// Function to show login container and hide game container
function showLoginContainer() {
    var loginContainer = document.getElementById('login-container');
    var gameContainer = document.getElementById('game-container');
    var quizContainer = document.getElementById('quiz-container');
     var resultContainer = document.getElementById('result-container');
    if (loginContainer && gameContainer && quizContainer && resultContainer) {
        loginContainer.style.display = 'flex';
        gameContainer.style.display = 'none';
        quizContainer.style.display = 'none'; // Hide quiz container
        resultContainer.style.display='none';
    }
}

// Function to show game container and hide login container
function showGameContainer() {
    var loginContainer = document.getElementById('login-container');
    var gameContainer = document.getElementById('game-container');
    var quizContainer = document.getElementById('quiz-container');
     var resultContainer = document.getElementById('result-container');
    if (loginContainer && gameContainer && quizContainer && resultContainer) {
        loginContainer.style.display = 'none';
        resultContainer.style.display='none';
        gameContainer.style.display = 'flex';
        quizContainer.style.display = 'none'; // Hide quiz container
        
    }
}

// Function to show quiz container and hide other containers
function showQuizContainer() {
    var loginContainer = document.getElementById('login-container');
    var gameContainer = document.getElementById('game-container');
    var quizContainer = document.getElementById('quiz-container');
     var resultContainer = document.getElementById('result-container');
    if (loginContainer && gameContainer && quizContainer && resultContainer) {
        loginContainer.style.display = 'none';
        gameContainer.style.display = 'none';
        resultContainer.style.display='none';
        quizContainer.style.display = 'block'; // Show quiz container
        
    }
}

function showResultContainer(marks, wrongAnswer) {
    var loginContainer = document.getElementById('login-container');
    var gameContainer = document.getElementById('game-container');
    var quizContainer = document.getElementById('quiz-container');
    var resultContainer = document.getElementById('result-container');
    
    if (loginContainer && gameContainer && quizContainer && resultContainer) {
        loginContainer.style.display = 'none';
        gameContainer.style.display = 'none';
        quizContainer.style.display = 'none'; 
        resultContainer.style.display='flex';
        resultContainer.innerHTML = ''; // Clear previous content

        // Add heading text
        var headingText = document.createElement('h1');
        headingText.textContent = 'Great!!! You have completed all the quiz \n';
        resultContainer.appendChild(headingText);

        // Display marks and wrong answers
        var marksText = document.createElement('p');
        marksText.textContent = 'Marks: ' +"  "+ marks;
        resultContainer.appendChild(marksText);

        var wrongAnswersText = document.createElement('p');
        wrongAnswersText.textContent = 'Wrong Answers: '+" " + wrongAnswer;
        resultContainer.appendChild(wrongAnswersText);

        // Create replay and leave room buttons
        var replayButton = document.createElement('button');
        replayButton.textContent = 'Replay';
        replayButton.addEventListener('click', function() {
            // Add logic to replay the quiz (e.g., reset quiz state and show quiz container)
            resetQuizState(); // Example function to reset quiz state
            showQuizContainer(); // Example function to show quiz container
        });
        replayButton.style.backgroundColor = '#007bff'; // Apply blue button style
        replayButton.style.border = 'none';
        replayButton.style.color = 'white';
        replayButton.style.padding = '10px 20px';
        replayButton.style.textAlign = 'center';
        replayButton.style.textDecoration = 'none';
        replayButton.style.display = 'inline-block';
        replayButton.style.fontSize = '16px';
        replayButton.style.margin = '4px 2px';
        replayButton.style.cursor = 'pointer';

        var leaveRoomButton = document.createElement('button');
        leaveRoomButton.textContent = 'Leave Room';
        leaveRoomButton.addEventListener('click', function() {
            // Add logic to leave the room and return to login or home screen
            leaveRoom(); // Example function to leave room
        });
        leaveRoomButton.style.backgroundColor = '#007bff'; // Apply blue button style
        leaveRoomButton.style.border = 'none';
        leaveRoomButton.style.color = 'white';
        leaveRoomButton.style.padding = '10px 20px';
        leaveRoomButton.style.textAlign = 'center';
        leaveRoomButton.style.textDecoration = 'none';
        leaveRoomButton.style.display = 'inline-block';
        leaveRoomButton.style.fontSize = '16px';
        leaveRoomButton.style.margin = '4px 2px';
        leaveRoomButton.style.cursor = 'pointer';

        // Append buttons to result container
        resultContainer.appendChild(replayButton);
        resultContainer.appendChild(leaveRoomButton);
    }
}





window.onload = function() {
    // Add submit listener to the login form if it exists
    var loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", onFormSubmit);
    }

    // Add click listener to the play-solo button if it exists
    var playSoloButton = document.getElementById("play-solo-button");
    if (playSoloButton) {
        playSoloButton.addEventListener("click", onPlaySoloClick);
    }
    
    var logoutbutton= document.getElementById("logout-button");
    if(logoutbutton){
		logoutbutton.addEventListener("click",onLogoutClick);
	}
    var config = {
        host: "127.0.0.1",
        port: 8080,
        zone: "BasicExamples"
    };

    sfs = new SFS2X.SmartFox(config);
};


function leaveRoom() {
    var lastJoinedRoom = sfs.lastJoinedRoom;

    if (lastJoinedRoom != null) {
        console.log("Leaving room: " + lastJoinedRoom.name);
        // Send a request to the server to leave the room
        sfs.send(new SFS2X.LeaveRoomRequest());
        console.log("Leave room request sent");
        showGameContainer();
    } else {
        console.log("No room to leave");
    }
}

function resetQuizState(){
	
	// Create an SFSObject for JSON parameters
    var jsonParams = new SFS2X.SFSObject();

    // Create an ExtensionRequest to send additional data to the room
    var extensionRequest = new SFS2X.ExtensionRequest('game.quiz_reset', jsonParams, sfs.lastJoinedRoom);

    // Send the ExtensionRequest to the room
    sfs.send(extensionRequest);
}
function onPlaySoloClick() {
    if (sfs) {
        joinSoloRoom();
    } else {
        console.log("Connecting to SmartFoxServer...");
        sfs = new SFS2X.SmartFox(config); // Initialize the SFS object
    }
}

function onLogoutClick(){
	
	 sfs.send(new SFS2X.LogoutRequest());
}


function joinSoloRoom() {
    window.sfs.send(new SFS2X.JoinRoomRequest("SoloGameRoom"));
    console.log("Join room request sent");
    showGameContainer();
}

// Public methods for UI
function onFormSubmit(event) {
    event.preventDefault(); // Prevent the form from submitting normally

    // Retrieve username and password from the form
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // You can perform validation here before proceeding to login
    if (username && password) {
        // Here you can perform login logic
        // Example: Call a function to handle login process
       
        handleLogin(username, password);
    } else {
        alert("Please enter both username and password.");
    }
}
 
function handleLogin(username, password) {
    let config = {
        host: "127.0.0.1",
        port: 8080,
        zone: "BasicExamples"
    };

    sfs = new SFS2X.SmartFox(config);

    // Add room-related event listeners during the SmartFox instance setup
   sfs.addEventListener(SFS2X.SFSEvent.CONNECTION, onConnection, this);
    sfs.addEventListener(SFS2X.SFSEvent.CONNECTION_LOST, onConnectionLost, this);
    sfs.addEventListener(SFS2X.SFSEvent.LOGIN, onLogin, this);
    sfs.addEventListener(SFS2X.SFSEvent.LOGIN_ERROR, onLoginError, this);
    sfs.addEventListener(SFS2X.SFSEvent.EXTENSION_RESPONSE, onExtensionResponse, this);
    sfs.addEventListener(SFS2X.SFSEvent.ROOM_JOIN, onRoomJoin, this);
    sfs.addEventListener(SFS2X.SFSEvent.ROOM_JOIN_ERROR, onRoomJoinError, this);
    sfs.addEventListener(SFS2X.SFSEvent.LEAVE_ROOM, onleaveRoom, this);
   
   
    
    // Add event listeners
    sfs.addEventListener(SFS2X.SFSEvent.CONNECTION, function(evt) {
        if (evt.success) {
            console.log("Connection established successfully");
            console.log("SFS2X API version: " + sfs.version);

            // Send user details to server extension
            var params = new SFS2X.SFSObject();
            params.putUtfString("username", username);
            params.putUtfString("password", password);
            sfs.send(new SFS2X.LoginRequest(username, password, params, config.zone));
            console.log(username + " " + password);
            console.log("login request sent");
        } else {
            console.error("Connection failed; is the server running at all?");
            // Remove SFS2X listeners and re-enable button
            reset();
        }
    });

    sfs.addEventListener(SFS2X.SFSEvent.CONNECTION_LOST, function(evt) {
        console.warn("Connection was lost; reason is: " + evt.reason);
        // Remove SFS2X listeners and re-enable button
        reset();
    });

    // Connect to SFS2X
    console.log("Now connecting...");
    sfs.connect();
    window.sfs = sfs;
}

// SmartFoxServer event listeners
function onLogin(evtParams) {
    console.log("Logged in as: " + sfs.mySelf.name);
    console.log(evtParams.success);
	
    console.log("Login successful");
        
    // Start the keep alive mechanism after successful login
    startKeepAlive();
}

function onLoginError(evtParams) {
    console.log("Login error: " + evtParams.errorMessage);
}

// Define function to handle zone join event
function onZoneJoin(evtParams) {
    console.log("Zone joined: " + sfs.currentZone.name);
    // You can perform additional actions here, such as retrieving user quiz history
}

function onExtensionResponse(evtParams) {
    var cmd = evtParams.cmd; // Command identifier sent from the server
    var params = evtParams.params; // Data sent from the server

    console.log("Received command:", cmd);
    
    // Handle response based on the command
    switch (cmd) {
        case "quizDataCommand":
			
			 var quizRecords = params.getSFSArray("quizRecords");
			 
			 var quizData = [];
            for (var i = 0; i < quizRecords.size(); i++) {
            var record = quizRecords.getSFSObject(i);
            var quizId = record.getInt("quizId");
            var quizName = record.getUtfString("quizName");
            var score = record.getInt("score");
            var Date_time = record.getUtfString("Date_time");
            
            // Add record to quizData array
            quizData.push({
                quizId: quizId,
                quizName: quizName,
                score: score,
                Date_time: Date_time
            });

            // Example: Log the record
            console.log("Quiz ID:", quizId);
            console.log("Quiz Name:", quizName);
            console.log("Score:", score);
            console.log("Date_time:", Date_time);
        }
            
            //sessionStorage.setItem("quizData", JSON.stringify(quizData));
            
            showGameContainer();
            break;
            
            
        case "quiz_question":
            // Display the received question
            var question = params.getUtfString("question");
            console.log("Received question:", question);
            // Update UI to display the question
            showQuizContainer();
            var quizContainer = document.getElementById('quiz-container');
            if (quizContainer) {
                quizContainer.innerHTML = '<h2>' + question + '</h2>';
                // Create option buttons A, B, C, D
                var options = ['A', 'B', 'C', 'D'];
                options.forEach(function(option) {
                    var button = document.createElement('button');
                    button.textContent = option;
                    button.className = 'option-button';
                    button.addEventListener('click', function() {
                        sendResponse(option);
                        resetTimer(); // Reset the timer after sending the response
                    });
                    quizContainer.appendChild(button);
                });
                var timerElement = document.createElement('div');
                timerElement.id = 'timer';
                quizContainer.appendChild(timerElement);
                var timerValue = 15; // Timer value in seconds
                updateTimer(timerValue);
                var timerInterval = setInterval(function() {
                    timerValue--;
                    updateTimer(timerValue);
                    if (timerValue <= 0) {
                        clearInterval(timerInterval);
                        // Time's up, handle accordingly (e.g., send a default response)
                    }
                }, 1000);
                // Store the timer interval ID
                quizContainer.dataset.timerIntervalId = timerInterval;
            
    }
            
            break;
            
          case "quiz_finished":
    // Display marks and wrong answers received from the server
    var marks = params.getInt("marks");
    console.log("Received marks:", marks);
    var wrongAnswer = params.getInt("wrongAnswers"); // Corrected the variable name to match what's sent from the server
    console.log("Wrong Answers:", wrongAnswer);

    // Show the result container with marks and wrong answers
    showResultContainer(marks, wrongAnswer);

    // Hide the game container
    var gameContainer = document.getElementById('game-container');
    if (gameContainer) {
        gameContainer.style.display = 'none';
    }

    // Store marks and wrong answers in session storage if needed
   // sessionStorage.setItem("quizMarks", marks);
   // sessionStorage.setItem("wrongAnswers", wrongAnswer);
         
         break;
          
          case "leaveRoom":
            var status = params.getBool("success");
            console.log("Leave room status:", status);
            var roomName = params.getUtfString("roomName");
            console.log("Left room name:", roomName);
            break;
       }

      
}



function updateTimer(seconds) {
    var timerElement = document.getElementById('timer');
    if (timerElement) {
        timerElement.textContent = seconds + ' seconds remaining';
    }
}
// Function to reset the timer
function resetTimer() {
    var quizContainer = document.getElementById('quiz-container');
    if (quizContainer) {
        // Clear the previous timer interval
        var timerIntervalId = quizContainer.dataset.timerIntervalId;
        clearInterval(timerIntervalId);
        // Remove the timer element
        var timerElement = document.getElementById('timer');
        if (timerElement) {
            timerElement.parentNode.removeChild(timerElement);
        }
    }
}
function onConnection(evt) {
    if (evt.success) {
        console.log("Connection established successfully");
        console.log("SFS2X API version: " + sfs.version);
    } else {
        console.error("Connection failed; is the server running at all?");
        // Remove SFS2X listeners and re-enable button
        reset();
    }
}

function onLogout(evt){
	if(evt.success){
		console.log("logging out user")
	}else{
		console.log("cant logout")
	}
}

// Event listener for the history button
document.getElementById("history-button").addEventListener("click", function(event) {
    event.preventDefault(); // Prevent the default behavior of the anchor tag
    var quizData = sessionStorage.getItem("quizData");
    if (quizData) {
        var encodedData = encodeURIComponent(quizData);
        window.location.href = "history.html?data=" + encodedData;
    } else {
        alert("No quiz data available.");
    }
});

function onConnectionLost(evt) {
    console.warn("Connection was lost; reason is: " + evt.reason);
    // Remove SFS2X listeners and re-enable button
    reset();
}

function onRoomJoin(evt) {
    console.log("Room joined: " + evt.room.name);
    
    // Create ISFSObject for JSON parameters
    var jsonParams = new SFS2X.SFSObject();
    jsonParams.putInt("param1", 123); // Example parameter 1
   
    // Get the room name
    var roomName = evt.room.name;
    
    // Create an ExtensionRequest to send additional data to the room
    var extensionRequest = new SFS2X.ExtensionRequest('game.quiz_question', jsonParams, sfs.getRoomByName(roomName));

    // Send the ExtensionRequest to the room
    sfs.send(extensionRequest);

    console.log("Custom extension request 'game.userturn' sent to room 'SoloGameRoom'");
}

function onleaveRoom(event) {
    console.log("Left room: ", event.room.name);
    showGameContainer(); // Redirect to login or home screen after leaving the room
}


function onRoomJoinError(evt) {
    console.warn("Room join failed: " + evt.errorMessage);
}

// Private helper methods
function reset() {
    // Remove SFS2X listeners
    sfs.removeEventListener(SFS2X.SFSEvent.CONNECTION, onConnection);
    sfs.removeEventListener(SFS2X.SFSEvent.CONNECTION_LOST, onConnectionLost);
    sfs.removeEventListener(SFS2X.SFSEvent.ROOM_JOIN, onRoomJoin);
     sfs.removeEventListener(SFS2X.SFSEvent.LOGOUT, onLogoutClick);
    
    sfs = null;
    // Enable button
    document.getElementById("connectBt").disabled = true;
}


// Function to send response
// Function to send response
function sendResponse(option) {
    // Create the extension request object with the command identifier and parameters
    var jsonParams = new SFS2X.SFSObject();
    jsonParams.putUtfString("option", option); // Use putUtfString instead of putString
    jsonParams.putInt("clientGameStateKey", clientGameStateKey);
      
    // Send the extension request to the server with the custom command identifier
    var extensionRequest = new SFS2X.ExtensionRequest("game.option_selected", jsonParams, sfs.lastJoinedRoom);
    sfs.send(extensionRequest);

    console.log("Selected option:", option);
    
    // Increment the clientGameStateKey for the next response
    clientGameStateKey++;
    // You may want to handle UI updates or further actions based on the response
}



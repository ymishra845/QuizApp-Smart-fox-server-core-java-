package com.sfs.app.loginfile;

import java.util.EnumSet;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.ISFSGameApi;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomSettings;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.exceptions.SFSRoomException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.ISFSExtension;
import com.smartfoxserver.v2.game.CreateSFSGameSettings;

public class ServerReadyEventHandler extends BaseServerEventHandler {

	
	protected static final String GAME_EXTENSION_CLASS = "com.sfs.app.GameExtension";
	public static ISFSExtension zoneExtension;
//	 public  ServerReadyEventHandler(MyExtension zoneExtension) {
//		 
//		this.zoneExtension = zoneExtension;
//	}

	@Override
    public void handleServerEvent(ISFSEvent event) throws SFSCreateRoomException, SFSJoinRoomException, SFSRoomException {
        User user = (User) event.getParameter(SFSEventParam.USER);

        
        trace("Inside handler");

        // Get the "BasicExamples" zone
        SmartFoxServer sfs = SmartFoxServer.getInstance();
        Zone basicExamplesZone = sfs.getZoneManager().getZoneByName("BasicExamples");

        if (basicExamplesZone == null) {
            trace("BasicExamples zone not found");
            return;
        }

        // Create a new game room for solo play in the "BasicExamples" zone
        createSoloGameRoom(user, basicExamplesZone);
        
        
    }

    public ServerReadyEventHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	private void createSoloGameRoom(User user, Zone zone) throws SFSCreateRoomException, SFSJoinRoomException, SFSRoomException {
        SmartFoxServer sfs = SmartFoxServer.getInstance();
        ISFSGameApi api = SmartFoxServer.getInstance().getAPIManager().getGameApi();

        trace("Inside room creation method");

        // Create room settings
//        CreateRoomSettings settings = new CreateRoomSettings();
//        settings.setName("SoloGameRoom");
//        settings.setMaxUsers(1); // Only one player for solo play
//        settings.setDynamic(false); // Room will be removed when empty
//        settings.setGame(true); // Indicates that it's a game room
        

        CreateSFSGameSettings gameSettings = new CreateSFSGameSettings();
        gameSettings.setName("SoloGameRoom");
		gameSettings.setGroupId("default");
		gameSettings.setMinPlayersToStartGame(1);
		gameSettings.setMaxSpectators(10);
		gameSettings.setGamePublic(true);		
		
		
		
		
			gameSettings.setRoomSettings(EnumSet.of(SFSRoomSettings.ROOM_NAME_CHANGE,  SFSRoomSettings.PASSWORD_STATE_CHANGE,  SFSRoomSettings.PUBLIC_MESSAGES,  
					SFSRoomSettings.CAPACITY_CHANGE, SFSRoomSettings.USER_ENTER_EVENT, SFSRoomSettings.USER_EXIT_EVENT, SFSRoomSettings.USER_VARIABLES_UPDATE_EVENT));
        gameSettings.setExtension(new CreateSFSGameSettings.RoomExtensionSettings(zoneExtension.getName(), GAME_EXTENSION_CLASS));
    
        //settings.setExtension(new CreateRoomSettings.RoomExtensionSettings(GAME_EXTENSION_CLASS, null));
        // Create the room in the specified zone
        Room room = api.createGame(zoneExtension.getParentZone(), gameSettings, null,true,false);
        
        trace("room log is "+room.getName());

        if (room == null) {
            trace("Failed to create room");
            return;
        }

        trace("Room created with name: " + room.getName());
        trace("Room has been created");

       
        
         }

   
    private void sendQuizList(User user, Room room) {
        // Create a static quiz list
    	trace("inside quizList");
        ISFSObject quizList = new SFSObject();
        quizList.putInt("quizId", 1);
        quizList.putUtfString("quizName", "Quiz 1");
        quizList.putInt("score", 0);
        quizList.putInt("quizId", 2);
        quizList.putUtfString("quizName", "Quiz 2");
        quizList.putInt("score", 0);

        quizList.putUtfString("roomName", room.getName());
        quizList.putInt("maxUsers", room.getMaxUsers());
        // Send the quizList to the client
        getApi().sendExtensionResponse("quizListCommand", quizList, user, null, false);
        trace("Quiz list sent to user");
    }
}
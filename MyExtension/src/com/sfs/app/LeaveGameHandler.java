package com.sfs.app;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class LeaveGameHandler extends BaseServerEventHandler {
    
    private GameExtension gameExtension;

    public LeaveGameHandler(GameExtension gameExtension) {
        this.gameExtension = gameExtension;
    }

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        try {
            Boolean flag = false;
            trace("LeaveRoomGameHandler, Handling Server Event");

            if (event == null) {
                trace("LeaveRoomGameHandler, ISFSEvent is NULL. Returning from handleServerEvent().");
                return;
            }

            User user = (User) event.getParameter(SFSEventParam.USER);
            if (user == null) {
                trace("LeaveRoomGameHandler, User is NULL. Returning from handleServerEvent().");
                return;
            }

            trace("User is " + user.getName());

            Room room = getParentExtension().getParentRoom();
            if (room == null) {
                trace("LeaveRoomGameHandler, User " + user.getName() + " has no last joined room. Returning from handleServerEvent().");
                return;
            }

            trace("Room name is " + room.getName());
            trace("User " + user.getName() + " is leaving the room " + room.getName() + ".");
            getApi().leaveRoom(user, room, true, true);
            trace("gameRoom leave event executed");
            flag = true;

            MyExtension ext= (MyExtension) gameExtension.getParentZone().getExtension();
            ISFSObject params = new SFSObject();
            params.putBool("success", flag);
            params.putUtfString("roomName", room.getName());
            ext.send("leaveRoom", params, user);
        } catch (Exception e) {
            trace("LeaveRoomGameHandler, Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

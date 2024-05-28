package com.sfs.app.loginfile;

import com.smartfoxserver.v2.core.ISFSEvent;

import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;


public class LogoutReqEventHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
    	trace("Inside userLogout event handler");
        // Retrieve the user who triggered the event
    	User user = (User) event.getParameter(SFSEventParam.USER);
        user.disconnect(null);
        
        trace("User disconnected successfully");
    }
}

package com.sfs.app;

import com.sfs.app.loginfile.KeepAliveEventHandler;
import com.sfs.app.loginfile.LoginReqEventHandler;
import com.sfs.app.loginfile.LogoutReqEventHandler;
import com.sfs.app.loginfile.ServerReadyEventHandler;
import com.sfs.app.loginfile.ZoneJoinEventHandler;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.ISFSExtension;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MyExtension extends SFSExtension{

	  
	 
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
		 
		
        addEventHandler(SFSEventType.USER_LOGIN, LoginReqEventHandler.class);
        trace("Hello You are logged in");
        addEventHandler(SFSEventType.USER_LOGOUT, LogoutReqEventHandler.class);

        trace("Logout Event Handler");
       
        
          addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class);
          trace("ZoneJoin Event Handler");
//          ServerReadyEventHandler serverReadyHandler = new ServerReadyEventHandler();
//          serverReadyHandler.setParentExtension(this);
          addEventHandler(SFSEventType.SERVER_READY, ServerReadyEventHandler.class);
         
          trace("user in server ready event handler to create and join dynamic room");
        
        
        //add request handler for keep a live
          
          addRequestHandler("game", KeepAliveEventHandler.class);
          trace("Keep a live request sent");
          
          ServerReadyEventHandler.zoneExtension=this; 
          
         
         
        
	}

	

	
}

package com.sfs.app.loginfile;
import com.smartfoxserver.v2.annotations.MultiHandler;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@MultiHandler
public class KeepAliveEventHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
	    trace("KeepAlive request received" + params.getUtfString(SFSExtension.MULTIHANDLER_REQUEST_ID) );
	    trace(params.toJson());

	    if (params != null && params.containsKey(SFSExtension.MULTIHANDLER_REQUEST_ID)) {
	        String requestId = params.getUtfString(SFSExtension.MULTIHANDLER_REQUEST_ID);
	        
	        
	        
	        switch(requestId) {
	            case "keepAlive":
	                DoKeepAlive(sender, params);
	                break;
	            case "Remove":
	                // Handle 'Remove' case if needed
	                break;
	            default:
	                trace("Unknown request ID: " + requestId);
	        }
	    } else {
	        trace("Invalid or missing request parameters");
	    }
	}

	private void DoKeepAlive(User sender, ISFSObject params) {
	    // Do something with the keep-alive request
	    getApi().sendExtensionResponse("game.keepAlive", params, sender, null, false);
	    trace("keepAlive request sent to client");
	}


	
   
}

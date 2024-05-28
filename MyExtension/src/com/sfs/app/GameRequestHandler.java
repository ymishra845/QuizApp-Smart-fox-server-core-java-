package com.sfs.app;

import com.smartfoxserver.v2.annotations.MultiHandler;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

import extensions.games.common.Commands;

@MultiHandler
public class GameRequestHandler extends BaseClientRequestHandler{

	
	
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		// TODO Auto-generated method stub
		
		trace("username is "+ user.getName());
		
		trace("GameRequest handler");
		GameExtension parentExtension = (GameExtension)getParentExtension();
		
		trace("GameRequestHandler,Handling Client Request."); 
    	if(user==null || params==null)
    	{
    		trace("GameRequestHandler, Value of User or ISFSObject is NULL. Returning from GameRequestHandler.");
    		return;
    	}
    	
    	
		
    	 String requestId = params.getUtfString(SFSExtension.MULTIHANDLER_REQUEST_ID);
    	
    	switch (requestId) {
		case "quiz_question":
			parentExtension.sendNextQuestion();
			break;

		 case "option_selected":
			 trace(params.getUtfString("option"));
	            parentExtension.handleAnswer(user, params.getUtfString("option"),params.getInt("clientGameStateKey"));
	            break;
		 case "quiz_reset":
			 trace(params.getUtfString("option"));
	            parentExtension.reset_Quiz();
	            break;
	     
		default:
			break;
		}
	}
 
}

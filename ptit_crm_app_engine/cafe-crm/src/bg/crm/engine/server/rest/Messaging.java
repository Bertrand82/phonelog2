package bg.crm.engine.server.rest;

import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cafe.crm.engine.server.message.BeanMessageFactory;
import cafe.crm.engine.server.util.UtilMail;





@Path("/crm")
public class Messaging {

	private static final Logger logger = Logger.getLogger(Messaging.class.getName());


	@GET
	@Path("test1")
	@Produces(MediaType.TEXT_HTML)
	public String test1() {
		return "<html><head> </head> <body>OK</body> </html>";
	}
	/**
	 * 
	 * @param emailUser
	 * @param phonecall
	 * @param contactname
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("message")
	@Produces(MediaType.TEXT_PLAIN)
	public Response message( 
			@FormParam("message")  String message, 
			@FormParam("meta") String meta,
			@FormParam("name") String name,
			@FormParam("accounts") String accounts
			
			) throws Exception {
		logger.warning("message : "+message+" | meta : "+meta);
		BeanMessageFactory.instance.persist(message, meta, name,accounts);
		String  email ="bertrand.guiral@gmail.com";
		UtilMail.sendMessage("Cafe-crm message", ""+message+"\n |||"+meta+"\n ||"+name, email);
		int code = 200;
		return  Response.status(code).header("bg2", "message").build();
	}
}

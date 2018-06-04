package paco01;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;



public class RestEP extends AbstractVerticle{
	
	
	private SQLClient mySQLClient;

	public void start(Future<Void> startFuture) {
		
		
		JsonObject mySQLClientConfig = new JsonObject()
				.put("host", "127.0.0.1")
				.put("port", 3306)
				.put("database", "paco")
				.put("username", "root")
				.put("password", "4953")
				;
		
		mySQLClient = MySQLClient.createShared(vertx, mySQLClientConfig);
		
		Router router = Router.router(vertx);
		
		vertx.createHttpServer().requestHandler(router::accept).
		listen(8083, res -> {
			if (res.succeeded()) {
				System.out.println("Servidor REST desplegado");
				router.getWithRegex(".*(html|css|js|jpg|png|gif)$").handler(url-> {
					String link = url.normalisedPath();
					url.response().sendFile("webroot"+link);
				});
			}else {
				System.out.println("Error: " + res.cause());
			}
		});
		

		
		
		router.route("/*").handler(BodyHandler.create());
		router.post("/api/loginPuerta/").handler(this::logPuerta);
		router.get("/api/door/:id").handler(this::getOnePuerta);
		router.get("/api/doorDir/:dir").handler(this::getPuertabyDir);
		router.get("/api/terminal/:id").handler(this::getOneTerminal);
		router.delete("/api/door/:id").handler(this::deleteOnePuerta);
		router.delete("/api/terminal/:id").handler(this::deleteOneTerminal);
		
		router.put("/api/door").handler(this::putElementPuerta);
		router.put("/api/terminal").handler(this::putElementTerminal);	
	
	}




	// ------------------------------ GETONEs ------------------------------
	
	
	private void getOnePuerta(RoutingContext routingContext) {
		String paramStr = routingContext.request().getParam("id");
		if(paramStr != null) {
			try {
				int param = Integer.parseInt(paramStr);
				mySQLClient.getConnection(conn -> {
					if(conn.succeeded()) {
						SQLConnection connection = conn.result();
						String query = "SELECT idPuerta, estadoPuerta, direccionPuerta, contraPuerta, adminPuerta "
						+ "FROM puerta "
						+ "WHERE idPuerta = ?";
						
						JsonArray paramQuery = new JsonArray()
								.add(param);
						
						connection.queryWithParams(query, paramQuery, res -> {
							if (res.succeeded()) {
								routingContext.response().end(Json.encodePrettily(res.result().getRows()));
							}else {
								routingContext.response().setStatusCode(400).end(res.cause().toString());
							}
						});
					}else {
						routingContext.response().setStatusCode(400).end(conn.cause().toString());
					}
				});
			}catch (ClassCastException e) {
				routingContext.response().setStatusCode(400).end();
			}
		}else {
			routingContext.response().setStatusCode(400).end();
		}
	}
	
	private void logPuerta(RoutingContext routingContext) {
		String direccion = routingContext.request().getParam("dir");
		String contrasena = routingContext.request().getParam("contra");
		
		if(direccion != null) {
			
			try {
				mySQLClient.getConnection(conn -> {
					if(conn.succeeded()) {
						SQLConnection connection = conn.result();
						String query = "SELECT contraPuerta "
						+ "FROM puerta "
						+ "WHERE direccionPuerta = ?";
						
						JsonArray paramQuery = new JsonArray()
								.add(direccion);
						
						connection.queryWithParams(query, paramQuery, res -> {
							if (res.succeeded()) {
								String getContra = Json.encodePrettily(res.result().getRows().get(0).getValue("contraPuerta"));
								//if (getContra == contrasena) {
									//routingContext.reroute("webroot/gestionPuerta.html");
									//routingContext.response().end();
								//}
								routingContext.response().end(getContra);
							}else {
								routingContext.response().setStatusCode(400).end(res.cause().toString());
							}
						});
					}else {
						routingContext.response().setStatusCode(400).end(conn.cause().toString());
					}
				});
			}catch (ClassCastException e) {
				routingContext.response().setStatusCode(400).end();
			}
		}else {
			routingContext.response().setStatusCode(400).end();
		}
	}
	
	private void getPuertabyDir(RoutingContext routingContext) {
		String paramStr = routingContext.request().getParam("dir");
		if(paramStr != null) {
			try {
				mySQLClient.getConnection(conn -> {
					if(conn.succeeded()) {
						SQLConnection connection = conn.result();
						String query = "SELECT idPuerta, estadoPuerta, direccionPuerta, contraPuerta, adminPuerta "
						+ "FROM puerta "
						+ "WHERE direccionPuerta = ?";
						
						JsonArray paramQuery = new JsonArray()
								.add(paramStr);
						
						connection.queryWithParams(query, paramQuery, res -> {
							if (res.succeeded()) {
								routingContext.response().end(Json.encodePrettily(res.result().getRows()));
							}else {
								routingContext.response().setStatusCode(400).end(res.cause().toString());
							}
						});
					}else {
						routingContext.response().setStatusCode(400).end(conn.cause().toString());
					}
				});
			}catch (ClassCastException e) {
				routingContext.response().setStatusCode(400).end();
			}
		}else {
			routingContext.response().setStatusCode(400).end();
		}
	}
	
	
	
	private void getOneTerminal(RoutingContext routingContext) {
		String paramStr = routingContext.request().getParam("id");
		if(paramStr != null) {
			try {
				int param = Integer.parseInt(paramStr);
				mySQLClient.getConnection(conn -> {
					if(conn.succeeded()) {
						SQLConnection connection = conn.result();
						String query = "SELECT idTerminal, acierto, fecha, bloqueo, numIntento, puertaRelacionada "
						+ "FROM terminal "
						+ "WHERE idTerminal = ?";
						JsonArray paramQuery = new JsonArray()
								.add(param);
						connection.queryWithParams(query, paramQuery, res -> {
							if (res.succeeded()) {
								routingContext.response().end(Json.encodePrettily(res.result().getRows()));
							}else {
								routingContext.response().setStatusCode(400).end(res.cause().toString());
							}
						});
					}else {
						routingContext.response().setStatusCode(400).end(conn.cause().toString());
					}
				});
			}catch (ClassCastException e) {
				routingContext.response().setStatusCode(400).end();
			}
		}else {
			routingContext.response().setStatusCode(400).end();
		}
	}
	
	
	
	
	
	
	
	
	
	// ------------------------------ DELETEs ------------------------------
	
	private void deleteOneTerminal(RoutingContext routingContext) {
		String paramStr = routingContext.request().getParam("id");
		if(paramStr != null) {
			try {
				int param = Integer.parseInt(paramStr);
				mySQLClient.getConnection(conn -> {
					if(conn.succeeded()) {
						SQLConnection connection = conn.result();
						String query = "DELETE FROM terminal "
						+ "WHERE idTerminal = ?";
						JsonArray paramQuery = new JsonArray()
								.add(param);
						connection.queryWithParams(query, paramQuery, res -> {
							if (res.succeeded()) {
								routingContext.response().end(Json.encodePrettily(res.result().getRows()));
							}else {
								routingContext.response().setStatusCode(400).end(res.cause().toString());
							}
						});
					}else {
						routingContext.response().setStatusCode(400).end(conn.cause().toString());
					}
				});

			}catch (ClassCastException e) {
				routingContext.response().setStatusCode(400).end();
			}
		}else {
			routingContext.response().setStatusCode(400).end();
		}
	}
	
	
	
	private void deleteOnePuerta(RoutingContext routingContext) {
		String paramStr = routingContext.request().getParam("id");
		if(paramStr != null) {
			try {
				int param = Integer.parseInt(paramStr);
				mySQLClient.getConnection(conn -> {
					if(conn.succeeded()) {
						SQLConnection connection = conn.result();
						String query = "DELETE FROM puerta "
						+ "WHERE idPuerta = ?";
						JsonArray paramQuery = new JsonArray()
								.add(param);
						connection.queryWithParams(query, paramQuery, res -> {
							if (res.succeeded()) {
								routingContext.response().end("Successfully deleted.");
							}else {
								routingContext.response().setStatusCode(400).end(res.cause().toString());
							}
						});
					}else {
						routingContext.response().setStatusCode(400).end(conn.cause().toString());
					}
				});
			}catch (ClassCastException e) {
				routingContext.response().setStatusCode(400).end();
			}
		}else {
			routingContext.response().setStatusCode(400).end();
		}
	}
	
	
	
	
	
	
	
	
	
	// ------------------------------ PUTs ------------------------------
	

	
	private void putElementPuerta(RoutingContext routingContext) {
		Puerta state = Json.decodeValue(routingContext.getBody().toString(), Puerta.class);		
			try {
				mySQLClient.getConnection(conn -> {
					if(conn.succeeded()) {
						SQLConnection connection = conn.result();
						String query = "INSERT INTO Puerta (idPuerta, estadoPuerta, direccionPuerta, contraPuerta, adminPuerta) "
						+ "VALUES (?, ?, ?, ?, ?) ";
						JsonArray paramQuery = new JsonArray()
								.add(state.getId())
								.add(state.getDoorState())
								.add(state.getDoorAddress())
								.add(state.getDoorPass())
								.add(state.getDoorAdmin());
						connection.queryWithParams(query, paramQuery, res -> {
							if (res.succeeded()) {
								routingContext.response().end(Json.encodePrettily(state));
							}else {
								routingContext.response().setStatusCode(400).end(res.cause().toString());
							}
						});	
					}else {
						routingContext.response().setStatusCode(400).end(conn.cause().toString());
					}
				});
			}catch (ClassCastException e) {
				routingContext.response().setStatusCode(400).end();
			}
	}
	
	
	
	private void putElementTerminal(RoutingContext routingContext) {
		Terminal state = Json.decodeValue(routingContext.getBody().toString(), Terminal.class);		
			try {
				mySQLClient.getConnection(conn -> {
					if(conn.succeeded()) {
						SQLConnection connection = conn.result();
						String query = "INSERT INTO Terminal (idTerminal, acierto, fecha, bloqueo, numIntento, puertaRelacionada) "
						+ "VALUES (?, ?, ?, ?, ?, ?) ";
						JsonArray paramQuery = new JsonArray()
								.add(state.getId())
								.add(state.isTryState())
								.add(state.getTryDate())
								.add(state.isTryBlock())
								.add(state.getTryNumber())
								.add(state.getRelatedDoor());
						connection.queryWithParams(query, paramQuery, res -> {
							if (res.succeeded()) {
								routingContext.response().end(Json.encodePrettily(state));
							}else {
								routingContext.response().setStatusCode(400).end(res.cause().toString());
							}
						});
					}else {
						routingContext.response().setStatusCode(400).end(conn.cause().toString());
					}
				});
			}catch (ClassCastException e) {
				routingContext.response().setStatusCode(400).end();
			}
	}
	
	
	
	
}

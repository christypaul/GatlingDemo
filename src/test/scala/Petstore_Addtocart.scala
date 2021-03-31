

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Petstore_Addtocart extends Simulation {

	val httpProtocol = http
		.baseUrl("http://petstore.smartload.io")


	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "text/css,*/*;q=0.1",
		"Proxy-Connection" -> "keep-alive")

	val headers_5 = Map("Proxy-Connection" -> "keep-alive")

	val headers_6 = Map(
		"Origin" -> "http://petstore.smartload.io",
		"Proxy-Connection" -> "keep-alive")

	val headers_15 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Cache-Control" -> "max-age=0",
		"Origin" -> "http://petstore.smartload.io",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val temp_username=csv("data/UserNamePasswordList.csv").circular // .queue , .circular , .random , .shuffle
	val temp_searchitem=csv("data/SearchCriteriaList.csv").circular

	val scn = scenario("AddToCart").repeat(2) {
		  //pace(300)
		  feed(temp_username)
  		.feed(temp_searchitem)
  		.exitBlockOnFail{
		// LaunchURL
		group("PetStore_AddToCart_T01_LaunchURL") {
			exec(http("request_0")
				.get("/applicationPetstore/shopping/main.xhtml")
				.headers(headers_0)
  				//.check(regex("</button><a href=\"(.*?)\" class=\"navbar-brand\">").find.saveAs("C_JsessionID"))
				//.check(regex("signon\\.xhtml;jsessionid=(.*?)\"").find.saveAs("C_jsessionId"))
				.check(status.is(200))
				.check(substring("<title>YAPS PetStore</title>").find.exists)
				.check(status.not(404))
				.check(status.in(200,201))
				.check(currentLocationRegex("shopping/main"))
				//.check(bodyBytes.is(RawFileBody("com/rerecordingPackage/recordedsimulationrerecording/0000_response.html")))
			)
		}

			.pause(2)
				// Tag_ClickLogIn
				.group("PetStore_AddToCart_T02_ClickLogInButton") {
					exec(http("Login_1")
						.get("/applicationPetstore/shopping/signon.xhtml;")
						.headers(headers_0)
						.check (regex ("javax.faces.ViewState:2\" value=\"(.+?)\" autocomplete=\"").find.saveAs ("C_ViewState")))
				}
			.pause(2)
			// Tag_ClickSignIn
			.group("PetStore_AddToCart_T03_ClickSignIn") {
				exec(http("Login_2")
					.post("/applicationPetstore/shopping/signon.xhtml")
					.headers(headers_15)
					.formParam("j_idt72", "j_idt72")
					.formParam("j_idt72:login", "admin")
					.formParam("j_idt72:password", "admin")
					.formParam("j_idt72:j_idt77", "Sign In")
					.formParam("javax.faces.ViewState", "${C_ViewState}")
					.check (regex ("javax.faces.ViewState:0\" value=\"(.+?)\" autocomplete=\"").find.saveAs ("C_ViewState"))
				)
			}
			.pause(2)
			// Tag_Search
			.group("PetStore_AddToCart_T04_SearchItem") {
				exec(http("request_19")
					.post("/applicationPetstore/shopping/main.xhtml")
					.headers(headers_15)
					.formParam("j_idt13", "j_idt13")
					.formParam("j_idt13:j_idt39", "${P_SearchItem}")
					.formParam("j_idt13:j_idt42", "Search")
					.formParam("javax.faces.ViewState", "${C_ViewState}")
  					.check(regex("showitem.xhtml\\?itemId=(.*?)\"").find.saveAs("C_SelectedItemID"))
  					.check(substring("Search for : ").find.exists)
					  .check(status.is(200))
					//.check(bodyBytes.is(RawFileBody("com/rerecordingPackage/recordedsimulationrerecording/0019_response.html")))
				)
			}
			.pause(2)
			// Tag_SelectItem
			.group("PetStore_AddToCart_T05_SelectItem") {
				exec(http("request_23")
					.get("/applicationPetstore/shopping/showitem.xhtml?itemId=${C_SelectedItemID}")
					.headers(headers_0)
					//.check(bodyBytes.is(RawFileBody("com/rerecordingPackage/recordedsimulationrerecording/0023_response.html")))
  					.check(regex("j_id1:javax.faces.ViewState:0\" value=\"(.*?)\"").find.saveAs("C_CartAddingItemID"))
  					.check(substring("Unit cost:").find.exists)
					  .check(status.is(200))
				)
			}
			.pause(2)
			// Tag_ClickAddToCart
			.group("PetStore_AddToCart_T06_ClickAddtoCart") {
				exec(http("request_27")
					.post("/applicationPetstore/shopping/showitem.xhtml")
					.headers(headers_15)
					.formParam("j_idt74", "j_idt74")
					.formParam("javax.faces.ViewState", "${C_CartAddingItemID}")
					.formParam("j_idt74:j_idt83", "j_idt74:j_idt83")
					.formParam("itemId", "${C_SelectedItemID}")
					.check(regex("j_id1:javax.faces.ViewState:0\" value=\"(.*?)\"").find.saveAs("C_CartAddedItemID"))
					.check(substring("Shopping Cart").find.exists)
					.check(status.is(200))
					//.check(bodyBytes.is(RawFileBody("com/rerecordingPackage/recordedsimulationrerecording/0027_response.html")))
				)
			}
			.pause(2)
			// tag_logOut
			.group("PetStore_AddToCart_T07_LogOut") {
				exec(http("request_31")
					//.post("/applicationPetstore/shopping/showcart.xhtml?cid=1")
					.post("/applicationPetstore/shopping/showcart.xhtml")
					.headers(headers_15)
					.formParam("j_idt13", "j_idt13")
					.formParam("j_idt13:j_idt39", "${P_SearchItem}")
					.formParam("javax.faces.ViewState", "${C_CartAddedItemID}")
					.formParam("j_idt13:j_idt29", "j_idt13:j_idt29")
  					.check(substring("Log in").find.exists)
					.check(status.is(200))
					//.check(bodyBytes.is(RawFileBody("com/rerecordingPackage/recordedsimulationrerecording/0031_response.html")))
				)
			}
	}
	}

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)

	//setUp(scn.inject(rampUsers(1)during(1))).protocols(httpProtocol)
	//setUp(scn.inject(rampUsers(3) during (15 second))).maxDuration(30 minutes).protocols(httpProtocol)

	//**********************************other User injection methods*************************
	//setUp(scn.inject(rampUsers(1)during(1))).protocols(httpProtocol)
	//setUp(scn.inject(nothingFor(10 seconds),rampUsers(1)during(1)).protocols(httpProtocol))


  	//.assertions(

			//global.responseTime.max.lt(2000),//globally giving assertions that all request response time should be less than 2 secs
		//	forAll.responseTime.max.lt(1000),//will print results of all transactions response time . if response time is less than 1 sec, will show as fail
		//  details("PetStore_AddToCart_T07_LogOut").responseTime.max.lt(1000)
	//	)
	//****************************************************************************************



}
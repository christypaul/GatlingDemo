
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Petstore_Basic extends Simulation {

	val httpProtocol = http
		.baseUrl("http://petstore.smartload.io")
		/*.disableFollowRedirect
		.inferHtmlResources(BlackList(), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36")*/


	val headers_0 = Map(
		"Accept-Auth" -> "badger,Wlid1.1,Bearer",
		"User-Agent" -> "Microsoft Office Excel 2014",
		"X-FeatureVersion" -> "1",
		"X-IDCRL_ACCEPTED" -> "t",
		"X-MS-CookieUri-Requested" -> "t",
		"X-Office-Major-Version" -> "16",
		"authorization" -> "Bearer")

	val headers_1 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "text/css,*/*;q=0.1",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9")

	val headers_7 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9")

	val headers_11 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Origin" -> "http://petstore.smartload.io")

	val headers_12 = Map(
		"Accept" -> "image/avif,image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9")

	val headers_28 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Origin" -> "http://petstore.smartload.io",
		"Upgrade-Insecure-Requests" -> "1")



	val scn = scenario("Basic").repeat(1) {



		// ********** Group transaction ********//

		// Launch //

		group("PetStore_LaunchURL") {


			// jsession id: y21wZAohmNEH2kx-0XVK0lZn_x3xX23BfEF1L6Ov.ip-172-31-82-187

			exec(http("LaunchURL_1")
				.get("/applicationPetstore/shopping/main.xhtml")
				.headers(headers_1))
				//.check(regex("jsessionid=(.+?)\" />").find.saveAs("C_jsessionId")))

		}


			// Login ///

			.group("PetStore_Login") {

				exec(http("Login_1")
					.get("/applicationPetstore/shopping/signon.xhtml;")
					.headers(headers_1)
				.check (regex ("javax.faces.ViewState:2\" value=\"(.+?)\" autocomplete=\"").find.saveAs ("C_ViewState")))


					.exec(http("Login_2")
						.post("/applicationPetstore/shopping/signon.xhtml")
						.headers(headers_28)
						.formParam("j_idt72", "j_idt72")
						.formParam("j_idt72:login", "admin")
						.formParam("j_idt72:password", "admin")
						.formParam("j_idt72:j_idt77", "Sign In")
						.formParam("javax.faces.ViewState", "${C_ViewState}")
						.check (regex ("javax.faces.ViewState:0\" value=\"(.+?)\" autocomplete=\"").find.saveAs ("C_ViewState"))
					)

			}



			// Logout //
			.group("PetStore_Logout") {

				exec(http("Logout_1")
					.post("/applicationPetstore/shopping/searchresult.xhtml")
					.headers(headers_28)
					.formParam("j_idt13", "j_idt13")
					.formParam("j_idt13:j_idt39", "fish")
					.formParam("javax.faces.ViewState", "${C_ViewState}")
					.formParam("j_idt13:j_idt29", "j_idt13:j_idt29")
					)
			}
	}
	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
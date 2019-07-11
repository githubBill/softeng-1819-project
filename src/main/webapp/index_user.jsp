<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ page import="gr.ntua.ece.softeng18b.conf.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*"  %>
<%@ page import="javax.servlet.ServletException" %>
<%@ page import="javax.servlet.http.*"  %>
<%@ page import="javax.servlet.RequestDispatcher" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Dirty Bits">

    <title>GG.spot</title>
	<link rel="shortcut icon" type="image/png" href="img/favicon.png"/>


    <!-- Bootstrap Core CSS -->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Merriweather:400,300,300italic,400italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>

    <!-- Plugin CSS -->
    <link href="vendor/magnific-popup/magnific-popup.css" rel="stylesheet">

    <!-- Theme CSS -->
    <link href="css/creative.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body id="page-top">

    <nav id="mainNav" class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">

            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span> Menu <i class="fa fa-bars"></i>
                </button>
			</div>



            <!--Anchor-->



            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a class="page-scroll" href="#search">Αναζήτηση</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#entry">Καταχώρηση</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#contact">Επικοινωνία</a>
                    </li>
                </ul>

				<ul class="nav navbar-nav navbar-left">
                    <li>
                      <script>
                      function getUrlVars() {
                        var vars = {};
                        var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
                            vars[key] = value;
                        });
                        return vars;
                      }
                      var usernameJS  = getUrlVars()["username"];
                      </script>
                        <a href="#myLogin" data-toggle = "modal" data-target= "#mySignOut" class="nav-link">
                        <!-- <script> document.write(usernameJS) </script>  -->
                        <%= request.getSession().getAttribute("sessionUser")  %>
                        </a>
                          <!-- <a href="#myModal" class="trigger-btn" data-toggle="modal">Login</a> -->

                    </li>

				</ul>

            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>


    <!-- Begin: Εδώ μπαίνει το modal για το SignOut Form -->
    <div class="modal fade" id="mySignOut">
        <div class="modal-dialog modal-login">
            <div class="modal-content">
                <div class="modal-header">
                   <h4 class="modal-title">Είσαι σίγουρος οτι θες να αποχωρήσεις;</h4>
					<!-- <button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button> -->
                </div>
                <div class="modal-body">
                    <form name="GoToStartingPage" action="./index.html">
                        <div class="form-group">
                            <input type="submit" class="btn btn-primary btn-lg btn-block login-btn" value="Αποσύνδεση">
<!-- <button type="submit" class="btn btn-primary btn-lg btn-block login-btn" value="login">ΣΥΝΔΕΣΗ</button> -->
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- End: Εδώ μπαίνει το modal για το SignOut Form -->

    <header>


	<div class="header-content">
		<div class="header-content-inner">
			<h1 id="homeHeading">GG.SPOT</h1>
			<hr>
			<h3> Καλώς όρισες <%= request.getSession().getAttribute("sessionUser") %> ! </h3>
			<br>
			<!--<p>Η πιο ολοκληρωμένη εμπειρία στον χώρο του gaming</p>
		-->
			<link href="https://fonts.googleapis.com/css?family=Raleway:200,100,400" rel="stylesheet" type="text/css" />
			<h2>The future is
			<span
				class="txt-rotate"
				data-period="2000"
				data-rotate='[ "you.", "us.", "GG Spot!" ]'></span>
			</h2>
			<!-- <h2>A single &lt;span&gt; is all you need.</h2> -->


			<a href="#search" class="btn btn-primary btn-xl page-scroll">Ψάξε σε αμέτρητα προϊόντα</a>
			<a href="#entry" class="btn btn-primary btn-xl page-scroll">Καταχώρησε κι εσύ!</a>
		</div>
	</div>


    </header>

    <section class="no-padding" id="search">
        <div class="container-fluid">
            <div class="row no-gutter popup-gallery">

                <div class="col-lg-4 col-sm-6">
					<div class="portfolio-box">
						<img src="img/laptop12.jpg" class="img-responsive" alt="">
						<div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                    Κατηγορια
                                </div>
								<form name="search_laptop" method="post" action="" >
									<input type="submit" value="Laptops" class="search_category">
								</form>
                            </div>
                        </div>
					</div>
                </div>


                <div class="col-lg-4 col-sm-6">
					<div class="portfolio-box">
						<img src="img/pc11.jpg" class="img-responsive" alt="">
						<div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                    Κατηγορια
                                </div>
								<form name="search_pc" method="post" action="" >
									<input type="submit" value="pc" class="search_category">
								</form>
                            </div>
                        </div>
					</div>
                </div>

                <div class="col-lg-4 col-sm-6">
					<div class="portfolio-box">
						<img src="img/consoles1.jpg" class="img-responsive" alt="">
						<div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                    Κατηγορια
                                </div>
								<form name="search_consoles" method="post" action="" >
									<input type="submit" value="Κονσολες" class="search_category">
								</form>
								<form name="search_controllers" method="post" action="" >
									<input type="submit" value="χειριστηρια" class="search_category">
								</form>
                            </div>
                        </div>
					</div>
                </div>


                <div class="col-lg-4 col-sm-6">
					<div class="portfolio-box">
						<img src="img/peripherals1.jpg" class="img-responsive" alt="">
						<div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                   Κατηγοριες Περιφερειακα
                                </div>
								<form name="search_keyboard" method="post" action="" >
									<input type="submit" value="Πληκτρολογια" class="search_category">
								</form>
								<form name="search_mouses" method="post" action="" >
									<input type="submit" value="Ποντικια" class="search_category">
								</form>
								<form name="search_speakers" method="post" action="" >
									<input type="submit" value="Ηχεια" class="search_category">
								</form>
								<form name="search_headsets" method="post" action="" >
									<input type="submit" value="Ακουστικα" class="search_category">
								</form>
                            </div>
                        </div>
					</div>
                </div>

                <div class="col-lg-4 col-sm-6">
					<div class="portfolio-box">
						<img src="img/monitor3.jpg" class="img-responsive" alt="">
						<div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                    Κατηγορια
                                </div>
								<form name="search_monitors" method="post" action="" >
									<input type="submit" value="Οθονες" class="search_category">
								</form>
                            </div>
                        </div>
					</div>
                </div>

                <div class="col-lg-4 col-sm-6">
					<div class="portfolio-box">
						<img src="img/games2.jpg" class="img-responsive" alt="">
						<div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                   Κατηγοριες Παιχνιδια
                                </div>
								<form name="search_action" method="post" action="" >
									<input type="submit" value="Action" class="search_category">
								</form>
								<form name="search_fps" method="post" action="" >
									<input type="submit" value="fps" class="search_category">
								</form>
								<form name="search_racing" method="post" action="" >
									<input type="submit" value="racing" class="search_category">
								</form>
								<form name="search_rpg" method="post" action="" >
									<input type="submit" value="rpg" class="search_category">
								</form>
								<form name="search_sports" method="post" action="" >
									<input type="submit" value="sports" class="search_category">
								</form>
								<form name="search_strategy" method="post" action="" >
									<input type="submit" value="strategy" class="search_category">
								</form>
                            </div>
                        </div>
					</div>
                </div>

            </div>
        </div>
    </section>



    <section id="entry">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <h2 class="section-heading">Καταχώρησε και κέρδισε</h2>
                    <hr class="primary">
                </div>
            </div>
        </div>


<!---------**********ΦΟΡΜΑ ΚΑΤΑΧΏΡΗΣΗΣΗΣ ΝΈΩΝ ΠΡΟΙΌΝΤΩΝ ΚΑΙ ΚΑΤΑΣΤΗΜΆΤΩΝ************----------->
		<div class="row">
			<div class="col-75">
				<div class="entry-container">
					<form name="make_entry" method="post" action="/insert" >
						<div class="row">
							<div class="col-50">
								<h3>Προϊόν</h3>
								<label for="brand"> Μάρκα</label>
								<select id="brand" name="brand" required="required">
									<option value=""></option>
									<option value="acer">Acer</option>
									<option value="asus">Asus</option>
									<option value="apple">Apple</option>
									<option value="bandai">Bandai Namco Entertainment</option>
									<option value="benq">BenQ</option>
									<option value="blizzard">Blizzard</option>
									<option value="capcom">Capcom</option>
									<option value="corsair">Corsair</option>
									<option value="creative">Creative</option>
									<option value="dell">Dell</option>
									<option value="electronicArts">Electronic Arts</option>
									<option value="gigabyte">Gigabyte</option>
									<option value="google">Google</option>
									<option value="hp">HP</option>
									<option value="konami">Konami</option>
									<option value="lenovo">Lenovo</option>
									<option value="logitech">Logitech</option>
									<option value="microsoft">Microsoft</option>
									<option value="msi">MSI</option>
									<option value="nintendo">Nintendo</option>
									<option value="perfectWorld">Perfect World</option>
									<option value="razer">Razer</option>
									<option value="roccat">ROCCAT</option>
									<option value="sega">Sega</option>
									<option value="sony">Sony</option>
									<option value="squareEnix">Square Enix</option>
									<option value="samsung">Samsung</option>
									<option value="sennheiser">Sennheiser</option>
									<option value="tencent">Tencent</option>
									<option value="ubisoft">Ubisoft</option>
									<option value="warner">Warner Bros</option>
									<option value="xseed">XSEED Games</option>
									<option value="other">Άλλο</option>
								</select>
								<!--<input type="text" id="brand" name="brand" placeholder="Λίστα">-->

								<label for="model"> Μοντέλο</label>
								<input type="text" id="model" name="model" required="required">

								<label for="category"> Κατηγορία Προϊόντος</label>
								<select id="category" name="category" required="required">
									<option value=""></option>
									<option value="laptops">Laptops</option>
									<option value="pcs">Σταθεροί Υπολογιστές</option>
									<option value="consoles">Κονσόλες</option>
									<option value="controllers">Χειριστήρια</option>
									<option value="keyboard">Πληκτρολόγια</option>
									<option value="mouses">Ποντίκια</option>
									<option value="speakers">Ηχεία</option>
									<option value="headsets">Ακουστικά</option>
									<option value="monitors">Οθόνες</option>
									<option value="actionGames">Παιχνίδια Action</option>
									<option value="fpsGames">Παιχνίδια FPS</option>
									<option value="racingGames">Παιχνίδια Racing</option>
									<option value="rpgGames">Παιχνίδια RPG</option>
									<option value="sportsGames">Παιχνίδια Sports</option>
									<option value="strategyGames">Παιχνίδια Strategy</option>
								</select>
								<!--<input type="text" id="category" name="category" placeholder="Λίστα">-->

								<label for="description"> Περιγραφή Προϊόντος</label>
								<input type="text" id="description" name="description" placeholder="Λοιπές πληροφορίες" required="required">

								<label for="productTag">Tags Προϊόντος</label>
								<input type="text" id="productTag" name="productTag" placeholder="Κινητό,Smartphone,4K(χωρισμένα από κόμμα)" required="required">

								<div class="row">
									<div class="col-50">
										<label for="price">Τιμή<i> (€)</i></label>
										<input type="text" id="price" name="price" placeholder="19,99" required="required">
									 </div>
									<!--
									<div class="col-50">
										<label for="zip">DATEfromDATEto</label>
										<input type="text" id="zip" name="zip" placeholder="10001">
									 </div>-->
								</div>
							</div>

						<div class="col-50">
							<h3>Κατάστημα</h3>
							<label for="shopName">Όνομα Καταστήματος</label>
							<input type="text" id="shopName" name="shopName" placeholder="Telemedia" required="required">

							<div class="row">
								<div class="col-50">
									<label for="city">Πόλη</label>
									<input type="text" id="city" name="city" placeholder="Αθήνα" required="required">
								</div>
								<div class="col-50">
									<label for="town">Περιοχή</label>
									<input type="text" id="town" name="town" placeholder="Ζωγράφου" required="required">
								</div>
							</div>

							<div class="row">
							<!--
								<div class="col-25">
									<label for="zip">Τ.Κ.</label>
									<input type="text" id="zip" name="zip" placeholder="15772">
								</div>
-->
								<div class="col-50">
									<label for="streetName">Οδός</label>
									<input type="text" id="streetName" name="streetName" placeholder="Βασιλίσσης Όλγας" required="required">
								</div>
								<div class="col-50">
									<label for="streetNumber">Αριθμός</label>
									<input type="text" id="streetNumber" name="streetNumber" placeholder="33" required="required">
								</div>
							</div>

							<label for="shopTag">Tags Καταστήματος</label>
							<input type="text" id="shopTag" name="shopTag" placeholder="Πολυκατάστημα,αλυσίδα, e-shop (χωρισμένα από κόμμα)" required="required">

							<div class="row">
								<div class="col-50">
										<label for="dateFrom">Από <b2><i>(ημερομηνία που άρχισε η προσφορά)</i></b2></label>
										<input type="date" id="dateFrom" name="dateFrom" required="required">
								</div>
								<div class="col-50">
									<label for="dateTo">Μέχρι <i><b2>(ημερομηνία που θα τελειώσει)</b2></i></label>
									<input type="date" id="dateTo" name="dateTo" placeholder="33" required="required">
								</div>
							</div>

						</div>
					</div>
					<input type="submit" value="Καταχώρησε" class="btn_entry">
				</form>
			</div>
		  </div>
		</div>
	</section>

<!--------********* ΕΠΙΚΟΙΝΩΝΙΑ ΜΑΖΙ ΜΑΣ********--------->
    <section id="contact">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 col-lg-offset-2 text-center">
                    <h2 class="section-heading">Επικοινώνησε μαζί μας!</h2>
                    <hr class="primary">
                    <p>Έχεις κάποια πρόταση; Κάποια απορία; Τέλεια! Μη διστάσεις να μας καλέσεις ή να μας στείλεις mail και να είσαι σίγουρος πως θα είμαστε δίπλα σου!</p>
                </div>
                <div class="col-lg-4 col-lg-offset-2 text-center">
                    <i class="fa fa-phone fa-3x sr-contact"></i>
                    <p>212-345-6789</p>
                </div>
                <div class="col-lg-4 text-center">
                    <i class="fa fa-envelope-o fa-3x sr-contact"></i>
                    <p><a href="mailto:your-email@your-domain.com">contact@ggspot.gr</a></p>
                </div>
            </div>
        </div>
    </section>

    <!-- jQuery -->
    <script src="vendor/jquery/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="vendor/bootstrap/js/bootstrap.min.js"></script>

    <!-- Plugin JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>
    <script src="vendor/scrollreveal/scrollreveal.min.js"></script>
    <script src="vendor/magnific-popup/jquery.magnific-popup.min.js"></script>


    <!-- Make an Entry JavaScript
    <script src="js/entry.js"></script> -->

    <!-- Theme JavaScript -->
    <script src="js/creative.js"></script>

</body>

</html>

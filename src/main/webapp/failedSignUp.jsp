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
                        <a href="#myLogin" data-toggle = "modal" data-target= "#myLogin" class="nav-link">εισοδος</a>
                        <!-- <a href="#myModal" class="trigger-btn" data-toggle="modal">Login</a> -->
                    </li>
                    <li>
                        <a href="#mySignUp" data-toggle = "modal" data-target= "#mySignUp" class="nav-link">εγγραφη</a>
                    </li>
				</ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>



    <!-- Begin: Εδώ μπαίνει το modal για το Login Form -->
    <div class="modal fade" id="myLogin">
        <div class="modal-dialog modal-login">
            <div class="modal-content">
                <div class="modal-header">
                    <div class="avatar">
                        <p style="text-align:center;"><img src="img/avatar3.jpg" alt="Avatar"></p>
                    </div>
                    <h4 class="modal-title">Είσοδος: Καλησπέρα μάγκα μου!</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                </div>
                <div class="modal-body">
                    <form name="loginForm" action="/login" method="post">
                        <div class="form-group">
                            <input type="text" class="form-control" name="username" placeholder="Ψευδώνυμο" required="required">
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" name="password" placeholder="Κωδικός Πρόσβασης" required="required">
                        </div>
                        <div class="form-group">
                            <input type="submit" class="btn btn-primary btn-lg btn-block login-btn" value="Συνδεση">
                            <!-- <button type="submit" class="btn btn-primary btn-lg btn-block login-btn" value="login">ΣΥΝΔΕΣΗ</button> -->
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <a href="#">Ξέχασα τον κωδικό μου</a>
                </div>
            </div>
        </div>
    </div>
    <!-- End: Εδώ μπαίνει το modal για το Login Form -->

    <!-- Begin: Εδώ μπαίνει το modal για το SignUp Form -->
    <div class="modal fade" id="mySignUp">
        <div class="modal-dialog modal-login">
            <div class="modal-content">
                <div class="modal-header">
                    <div class="avatar">
                        <p style="text-align:center;"><img src="img/avatar3.jpg" alt="Avatar"></p>
                    </div>
                    <h4 class="modal-title">Εγγραφή: Join Us We have cookies</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                </div>
                <div class="modal-body">
                    <form name="signUpForm" action="/signUp" method="post">
                        <div class="form-group">
                            <input type="text" class="form-control" name="name" placeholder="Όνομα" required="required">
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" name="surname" placeholder="Επώνυμο" required="required">
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" name="nickname" placeholder="Ψευδώνυμο" required="required">
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" name="email" placeholder="Email" required="required">
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" name="password" placeholder="Κωδικός Πρόσβασης" required="required">
                        </div>
                        <div class="form-group">
                            <input type="submit" class="btn btn-primary btn-lg btn-block login-btn" value="Υποβολη">
                            <!-- <button type="submit" class="btn btn-primary btn-lg btn-block login-btn" value="signUp">Υποβολή</button> -->
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- End: Εδώ μπαίνει το modal για το SignUp Form -->


    <header>


	<div class="header-content">
		<div class="header-content-inner">
			<h1 id="homeHeading">GG.SPOT</h1> </br>
      <h1 id="homeHeading"> Mr/Mrs <%= request.getAttribute("name") %>  <%= request.getAttribute("surname") %>  bad signup form! </h1>

			<h3>Γίνε μέλος τώρα!</h3>
			<br>
<!--			<p>Η πιο ολοκληρωμένη εμπειρία στον χώρο του gaming</p>
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


<!--
    <section class="bg-primary" id="search">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 col-lg-offset-2 text-center">
                    <h2 class="section-heading">We've got what you need!</h2>
                    <hr class="light">
                    <p class="text-faded">Start Bootstrap has everything you need to get your new website up and running in no time! All of the templates and themes on Start Bootstrap are open source, free to download, and easy to use. No strings attached!</p>
                    <a href="#entry" class="page-scroll btn btn-default btn-xl sr-button">Get Started!</a>
                </div>
            </div>
        </div>
    </section>
-->

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
        <div class="container">
            <div class="row">
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-diamond text-primary sr-icons"></i>
                        <h3>Κρυμμένες προσφορές</h3>
                        <p class="text-muted">Βρες εδώ τις προσφορές που σου κρύβουν</p>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-paper-plane text-primary sr-icons"></i>
                        <h3>Ανέβασε</h3>
                        <p class="text-muted">Καταχώρησε τες να μην είσαι μόνος σου και κέρδισε πόντους!</p>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-newspaper-o text-primary sr-icons"></i>
                        <h3>Πάντα πρώτοι</h3>
                        <p class="text-muted">Το μεγάλο δίκτυο που εσύ δημιουργείς σου εγγυάται συνεχώς τις καλύτερες τιμές</p>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-heart text-primary sr-icons"></i>
                        <h3>Από σένα για σένα</h3>
                        <p class="text-muted">Όσο πιο πολύ συμμετέχεις τόσο μεγαλύτερες και οι ευκαιρίες!</p>
                    </div>
                </div>
            </div>
        </div>
		<div class="btn-container">
			<a href="#mySignUp" data-toggle = "modal" data-target= "#mySignUp" class="btn btn-primary btn-xl2 page-scroll">ΚΑΝΕ ΕΓΓΡΑΦΗ ΤΩΡΑ!</a>
		</div>
    </section>

<!--
    <aside class="bg-dark">
        <div class="container text-center">
            <div class="call-to-action">
                <h2>Free Download at Start Bootstrap!</h2>
                <a href="http://startbootstrap.com/template-overviews/creative/" class="btn btn-default btn-xl sr-button">Download Now!</a>
            </div>
        </div>
    </aside>
-->

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

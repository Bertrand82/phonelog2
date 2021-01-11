<!DOCTYPE html>
<html lang="en">

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="utf-8"%>



<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${pageContext.request.locale}" />
<fmt:setBundle basename="cafe.crm.engine.page_index" />




<head>
<meta charset="utf-8">
<title>Cafe-crm</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="phone-log">
<meta name="author" content="bertrand">

<!-- Le styles -->
<link href="/assets/css/bootstrap.css" rel="stylesheet">
<link href="/assets/css/bootstrap-responsive.css" rel="stylesheet">
<link href="/assets/css/docs.css" rel="stylesheet">
<link href="/assets/js/google-code-prettify/prettify.css" rel="stylesheet">

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="assets/js/html5shiv.js"></script>
    <![endif]-->

<!-- Le fav and touch icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="assets/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="assets/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="assets/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed" href="assets/ico/apple-touch-icon-57-precomposed.png">
<link rel="shortcut icon" href="assets/ico/favicon.png">

<script type="text/javascript">
	var _gaq = _gaq || [];
	_gaq.push([ '_setAccount', 'UA-146052-10' ]);
	_gaq.push([ '_trackPageview' ]);
	(function() {
		var ga = document.createElement('script');
		ga.type = 'text/javascript';
		ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl'
				: 'http://www')
				+ '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(ga, s);
	})();
</script>
</head>

<body data-spy="scroll" data-target=".bs-docs-sidebar">

<a href="/r/crm/test1"> test</a>
	<!-- Navbar
    ================================================== -->
	<div class="navbar navbar-inverse navbar-fixed-top">		
			<%@include file="include_navbar_no_connected.jsp" %>
	</div>


	<!-- Subhead ================================================== -->
	

	<div class="container">


<div class="container">

		<!-- Docs nav
    ================================================== -->
		<div class="row">
			<div class="span3 bs-docs-sidebar">
				<div class="row">
					<div class="span3">
						<ul class="nav nav-list bs-docs-sidenav">
							<li><a href="/phone-log-android.apk"><img src="images/android_logo.gif" alt="android"><fmt:message key="download"/></a> </li>
							<li><a href="https://play.google.com/store/apps/details?id=phone.trace"> <img src="images/google_play_2.jpg" alt="android"> </a>             
						</ul>
					</div>
					
				</div>
				
			</div>

			<div class="span6">
				<section id="post">					
					<div class="page-header">
						<h2><fmt:message key="section_1_titre"/></h2>
						</div>
						<div> <fmt:message key="section_1_content_1"/></div>
						<div><fmt:message  key="section_1_content_2"/></div>
					    <div><fmt:message  key="section_1_content_3"/></div>
				
				</section>


				<section id="comment_ca_marche">
					<div class="page-header">
						<h2><fmt:message key="section_2_titre"/></h2>
					</div>
					<div><fmt:message key="section_2_content_1"/> </div> 
					<div> <fmt:message key="section_2_content_2"/></div>
					<div> <fmt:message key="section_2_content_3"/></div>
					
				</section>
				
			
				<section id="Private_policy">
					<div class="page-header">
						<h2><fmt:message key="section_3_titre"/> </h2>
					</div>
						<div><fmt:message key="section_3_content_1"/></div>
						
						
						<div><fmt:message key="section_3_content_2"/></div>
						
						<div><fmt:message key="section_3_content_3"/></div>
					
				</section>
				<section id="Prix">
					<div class="page-header">
						<h2> <fmt:message key="section_4_titre"/></h2>
					</div>
					<div><fmt:message key="section_4_content_1"/></div>
					<div><fmt:message key="section_4_content_2"/></div>
					<div><fmt:message key="section_4_content_3"/></div>
					
				</section>
				
			
			    <section id="keywords">
					<div class="page-header">
						<h2> <fmt:message key="section_5_titre"/></h2>
					</div>
					<div><fmt:message key="section_5_content_1"/></div>
					<div><fmt:message key="section_5_content_2"/></div>
					<div><fmt:message key="section_5_content_3"/></div>
					
				</section>
					<section id="use_case">
					<div class="page-header">
						<h2><fmt:message key="section_6_titre"/></h2>
					</div>
				   <div> <fmt:message key="section_6_content_1"/></div>
				   <p/>
				   <div><fmt:message  key="section_6_content_2"/></div>
				   <div><fmt:message  key="section_6_content_3"/></div>
						
				</section>
				
				<section id="use_case2">
					<div class="page-header">
						<h2><fmt:message key="section_7_titre"/></h2>
					</div>
				   <div> <fmt:message key="section_7_content_1"/></div>
				   <p/>
				   <div><fmt:message  key="section_7_content_2"/></div>
				   <div><fmt:message  key="section_7_content_3"/></div>
						
				</section>
				
				<section id="kw">
					<div class="page-header">
						<h2><fmt:message key="section_8_titre"/></h2>
					</div>
				   <div> <fmt:message key="section_8_content_1"/></div>
				   <p/>
				   <div><fmt:message  key="section_8_content_2"/></div>
				   <div><fmt:message  key="section_8_content_3"/></div>
						
				</section>
				
			
			</div>
			<div class="span3">
			</div>
			

		</div>

	
	</div>


				<!-- Footer
    ================================================== -->
				<footer class="footer">
					<div class="container">
						<p>Designed by cafe-crm</p>
					</div>
				</footer>

</div>

				<!-- Le javascript
    ================================================== -->
				<!-- Placed at the end of the document so the pages load faster -->
				<script type="text/javascript"
					src="http://platform.twitter.com/widgets.js"></script>
				<script src="assets/js/jquery.js"></script>
				<script src="assets/js/bootstrap-transition.js"></script>
				<script src="assets/js/bootstrap-alert.js"></script>
				<script src="assets/js/bootstrap-modal.js"></script>
				<script src="assets/js/bootstrap-dropdown.js"></script>
				<script src="assets/js/bootstrap-scrollspy.js"></script>
				<script src="assets/js/bootstrap-tab.js"></script>
				<script src="assets/js/bootstrap-tooltip.js"></script>
				<script src="assets/js/bootstrap-popover.js"></script>
				<script src="assets/js/bootstrap-button.js"></script>
				<script src="assets/js/bootstrap-collapse.js"></script>
				<script src="assets/js/bootstrap-carousel.js"></script>
				<script src="assets/js/bootstrap-typeahead.js"></script>
				<script src="assets/js/bootstrap-affix.js"></script>

				<script src="assets/js/holder/holder.js"></script>
				<script src="assets/js/google-code-prettify/prettify.js"></script>

				<script src="assets/js/application.js"></script>


			
</body>
</html>

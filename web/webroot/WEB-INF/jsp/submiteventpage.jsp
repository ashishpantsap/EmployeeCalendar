<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Submit event</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="jumbotron" style="height: 100%">
		<div class="container" >
			<form action="sendevent" method="post">
				<label>Select Inumber</label>
				<select name="inumber" class="form-control">
					<c:forEach var="employee" items="${employees}">
						<option value="${employee.PK}">${employee.inumber} &nbsp;&nbsp;-- &nbsp;&nbsp;
							${employee.name} &nbsp; ${employee.surname}</option>
					</c:forEach>
				</select>
				<!-- <label>Inumber</label><input type="text" name="inumber" required="required"/>  -->
				<label>Date</label>
				<input type="text" name="date" class="form-control"
					required="required"
					pattern="(0[1-9]|1[0-9]|2[0-9]|3[01])-(0[1-9]|1[012])-[0-9]{4}" 
					placeholder="dd-MM-yyyy"/>
				<br />	
				<button type="submit" class="btn btn-default btn-info">Submit</button>
			</form>
		</div>
	</div>
</body>
</html>
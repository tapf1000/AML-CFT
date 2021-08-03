/**
 * 
 */
/*<![CDATA[*/

	$(function(){
//------------------------------------------------------------------------------------------------------------------------------
		$("form[name='register']").submit(function(e){			
			if($("input[name='password']").text() != $("input[name='password2']").text()){
				e.preventDefault();
				alert("Passwords No Match!!! :\n -\n(");
			}
//			else {
//				$("form[name='register']").submit();
//				alert("Passwords N Match!!! :\n -\n)");
//			}
		});
//------------------------------------------------------------------------------------------------------------------------------
	});	
/*]]>*/
	
	
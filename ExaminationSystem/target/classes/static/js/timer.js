/**
 * 
 */
/*<![CDATA[*/
$(function() {
	//------------------------------------------------------------------------------------------------------------------------------
	var timer2 = /*[[${timer}]]*/$('#time').text();
	var interval = setInterval(function() {
	  var timer = timer2.split(':');
	  //by parsing integer, I avoid all extra string processing
	  var hours = parseInt(timer[0], 10);
	  var minutes = parseInt(timer[1], 10);
	  var seconds = parseInt(timer[2], 10);
	  --seconds;
	  hours = (minutes < 0) ? --hours : hours;
	  if (hours < 0) {
		  $('#myModal').modal('show');
		  clearInterval(interval);
		  setTimeout(function(){window.location.href = '/test';}, 2000); 		  
	  }
	  minutes = (minutes < 0) ? 59 : minutes;
	  minutes = (seconds < 0) ? --minutes : minutes;
	  
	  seconds = (seconds < 0) ? 59 : seconds;
	  seconds = (seconds < 10) ? '0' + seconds : seconds;
//	  minutes = (minutes < 10) ?  '0' + minutes : minutes;
//	  hours = (hours < 10) ?  '0' + hours : hours;
	  $('#time').text(hours + ':' + minutes + ':' + seconds);
	  $('#timer').val(hours+ ':' + minutes + ':' + seconds);
	  timer2 = hours+ ':' + minutes + ':' + seconds;
	}, 1000);
	//------------------------------------------------------------------------------------------------------------------------------	
});
/*]]>*/


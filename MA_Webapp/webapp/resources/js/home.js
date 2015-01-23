var gNEProblem = null;

function createTblHeader(numberOfAgent) {
	var header = "<tr>";
	header += "<th class='empty-cell'></th>"; //Empty cell
	
	for (var i = 0; i < numberOfAgent; i++) {	
		header += "<th>";
		header += "Agent ";
		header += (i + 1);
		header += "'s action";
		header += "</th>";
	}
	for (var i = 0; i < numberOfAgent; i++) {	
		header += "<th>";
		header += "Agent ";
		header += (i + 1);
		header += "'s utility";
		header += "</th>";
	}

	header += "</tr>";
	
	return $(header);
}

function createPreferenceRow(numberOfAgent, preferenceId) {
	var rowId = "preference_" + preferenceId;
	var $row = $("<tr></tr>");
	$row.attr("id", rowId);
	
	$row.append("<td>Preference " + preferenceId + "</td>");
	
	for (var i = 0; i < numberOfAgent; i++) {	
		var $td = $("<td></td>");
		var $input = $("<input type='text' />");
		$td.append($input);
		$row.append($td);
	}

	for (var i = 0; i < numberOfAgent; i++) {	
		var $td = $("<td></td>");
		var $input = $("<input type='text' />");
		var agentId = i + 1;
		$input.attr("id", rowId + "_" + "utility_" + agentId);
		$td.append($input);
		$row.append($td);
	}
	
	return $row;
}

function createNewPrefBtnRow(numberOfAgent) {
	var $btn = $("<input type='button' value='New preference'/>");
	$btn.click(function () {
		showNewPreferenceRow($(this), numberOfAgent);
	});
	
	var $btn_td = $("<td></td>");
	$btn_td.attr("colspan", 2 * numberOfAgent + 1);
	$btn_td.append($btn);
	
	var $btn_row = $("<tr></tr>");
	$btn_row.append($btn_td);
	return $btn_row;
}
	
function showNewPreferenceRow($btn, numberOfAgent) {
	applyResultsToRows([]);
	$new_pref_row = createPreferenceRow(numberOfAgent, getNumberOfPreferences() + 1);
	$btn.parent().parent().before($new_pref_row);
}

function getNumberOfPreferences() {
	var $table = $("#tbl_util");
	return $table.find("tr").length - 2;
}

function preSubmitPost () {
	if (gNEProblem != null) {
		fillInvalidCells();
		
		gNEProblem.clear();
		reconstructNEProblem();
		
		var emptyResults = [];
		applyResultsToRows(emptyResults);
	}
}

function showResult(data) {
	console.log(data);
	applyResultsToRows(data);
}

function fillInvalidCells() {
	$("#tbl_util td>input[type='text']").each(function (index, input) {
			var inputVal = $(input).val();
			if (inputVal != parseInt(inputVal, 10) ) {
				$(input).val(0);
			}				
	});
}

function reconstructNEProblem() {
	var numberOfPreference = getNumberOfPreferences();
	$("#tbl_util tr").each(function (index, row) {
		if (index >= 1 && index <= numberOfPreference) {
			console.log("Add new preference");
			gNEProblem.addPreferenceToUtilityMap($(row));
		}				
	});
}

function applyResultsToRows(resultRows) {
	var numberOfPreference = getNumberOfPreferences();
	$("#tbl_util tr").each(function (index, row) {
			if (index >= 1 && index <= numberOfPreference) {
				if (resultRows.indexOf(index) != -1) {
					$(row).children().each(function (index, cell) {
						$(cell).addClass("result-cell");
					});
				}
				else {
					$(row).children().each(function (index, cell) {
						$(cell).removeClass("result-cell");
					});
				}
			}				
	});
}

$(document).ready(function() {
		$("#btn_agent_number").click(function () {
			var $message = $("#p_message");
			$message.css("visibility", "visible");
			
			var $table = $("#tbl_util");
			$table.html("");
			var numberOfAgent = $("#txt_agent_number").val();
			gNEProblem = new NashEquilibriumProblem(numberOfAgent);
			var $header = createTblHeader(numberOfAgent);
			$table.append($header);
			var $row = createPreferenceRow(numberOfAgent, 1);
			$table.append($row);
			var $btn_row = createNewPrefBtnRow(numberOfAgent);
			$table.append($btn_row);
		});
		
		$("#btn_submit").click(function () {
			if (gNEProblem != null) {
				preSubmitPost();
				var requestString = JSON.stringify(gNEProblem);
				console.log(requestString);
				
				var url = "/agents-app/solve.do";
				$.ajax({
				  type: "POST",
				  url: url,
				  data: {"problem" : requestString},
				  success: function (data, txtStatus, jqXHR) {
				  		showResult(data);
				  },
				});
			}
		});
});
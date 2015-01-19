function parsePreferenceIdFromRowId(rowId) {
	return rowId.split("_")[1];
}

function parseInputValueFromCell($cell) {
	return $cell.find("input").eq(0).val();
}

function validateCellIndex(index, numberOfAgent) {
	return (index > 0 && index <= numberOfAgent);
}

function validateCellIndexForUtility(index, numberOfAgent) {
	return (index > numberOfAgent);
}

function Preference($row, numberOfAgent, preferenceIdParser, cellIndexValidator, actionIdParser) {
	this.id = preferenceIdParser($row.attr("id"));
	var actionMap = {};
	$row.children().each(function (index, cell) {
		if (cellIndexValidator(index, numberOfAgent)) {
			var agentId = index;
			var actionId = actionIdParser($(cell));
			actionMap[agentId] = actionId;
		}
	});
	this.actionMap = actionMap;
}

function NashEquilibriumProblem(numberOfAgent) {
	this.utilityMap = {};
	this.numberOfAgent = numberOfAgent;
	this.preferences = [];
}

NashEquilibriumProblem.prototype.addPreferenceToUtilityMap = function($row) {
	var preference = new Preference($row, this.numberOfAgent, parsePreferenceIdFromRowId, validateCellIndex, parseInputValueFromCell);
	this.preferences.push(preference);
		
	var utilityMap = this.utilityMap;
	utilityMap[preference.id] = {};
	
	var numberOfAgent = this.numberOfAgent;
	
	$row.children().each(function (index, cell) {
		if (validateCellIndexForUtility(index, numberOfAgent)) {
			var agentId = index - numberOfAgent;
			var utilityValue = parseInputValueFromCell($(cell));
			utilityMap[preference.id][agentId] = utilityValue;
		}
	});
	this.utilityMap = utilityMap;
}

NashEquilibriumProblem.prototype.clear = function () {
	this.preferences.splice(0, this.preferences.length);
	this.utilityMap = {};
}

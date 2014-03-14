1) Split by dot "." and remove all spaces on borders and empty split items
2) Split by comma "," and remove all spaces on borders and empty split items
3) Construct working hours for each split pattern:
	Week days pattern variants (first split after splitting by comma)
		- Todos dias da semana
		- <week day> à <week day>
		- <week day>
	
	Working hours pattern variants (second split after splitting by comma)
		- de <hh:mm> ate <hh:mm>
		- de <hh:mm> ate <hh:mm> e <hh:mm> ate <hh:mm>
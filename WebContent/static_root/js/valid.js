/* String 길이 체크*/
function getLength(str) {
	return(str.length+(escape(str)+"%u").match(/%u/g).length-1);
}

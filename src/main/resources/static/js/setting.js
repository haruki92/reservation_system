/** 画面ロード時に時間表記で0埋めする関数 */
window.onload = function setZeroPadding() {
	zeroPadding();
}

/** 時間の設定を変更した時に実行される関数 */
function zeroPadding() {
	let startHours = document.getElementById("startHours");
	let startMinutes = document.getElementById("startMinutes");
	let endHours = document.getElementById("endHours");
	let endMinutes = document.getElementById("endMinutes");

	startHours.value = setZero(startHours.value);
	endHours.value = setZero(endHours.value);
	startMinutes.value = setZero(startMinutes.value);
	endMinutes.value = setZero(endMinutes.value);
}

/** 2桁以下の場合は 0埋めした値を返す関数 */
function setZero(str) {
	return str.padStart(2, "0");
}

/** 開始時間を設定した後、終了時間を開始時間の１時間後以降に設定する関数 */
function setMinimumEndTimes() {
	let startHours = document.getElementById("startHours").value;
	let startMinutes = document.getElementById("startMinutes").value;
	let endHours = document.getElementById("endHours");
	let endMinutes = document.getElementById("endMinutes");

	let startTime = new Date(2022, 2, 2, startHours, startMinutes);
	let endTime = new Date(2022, 2, 2, endHours.value, endMinutes.value);

	let diff = (endTime.getTime() - startTime.getTime()) / (60 * 60 * 1000);

	endHours.min = parseInt(startHours, 10) + 1;


	/** 終了時間が開始時間より小さい = 早い場合に開始時間 + 1時間をセットする */
	if (diff <= 1) {
		console.log("終了時間を開始時間の１時間後に変更します");

		endHours.value = endHours.min;
		endMinutes.value = startMinutes;

		endMinutes.min = startMinutes;

		endHours.textContent = endHours.value;
		endMinutes.textContent = endMinutes.value;

	} else {
		endMinutes.min = 0;
	}

	zeroPadding();
}
/** 現在の年月を設定する */
window.onload = function setCurrentDate() {
	console.log("setCurrentDate()");
	/** 現在の年月を取得 */
	let currentDate = new Date();
	let currentYear = currentDate.getFullYear();
	let currentMonth = currentDate.getMonth() + 1;

	/** 年のセレクトボタンにオプションを追加 */
	let yearSelect = document.getElementById("yearSelect");

	for (let i = currentYear; i < currentYear + 5; i++) {
		let option = document.createElement("option");

		option.value = i;
		option.text = i;

		if (i === currentYear) {
			/** 現在の年を選択する */
			option.selected = true;
		}
		yearSelect.add(option);
	}

	let monthSelect = document.getElementById("monthSelect");

	for (let i = 1; i <= 12; i++) {
		let option = document.createElement("option");

		option.value = i;
		option.text = i;

		if (i === currentMonth) {
			/** 現在の月を選択する */
			option.selected = true;
		}
		monthSelect.add(option);
	}

	getReservationsByYearAndMonth();

}



/** 年月を条件に予約リストを取得して、table要素を作成するメソッド */
function getReservationsByYearAndMonth() {
	console.log("getReservationsByYearAndMonth()");
	/** yearとmonthのセレクトボタンの値を取得する */
	let yearSelect = document.getElementById("yearSelect").value;
	let monthSelect = document.getElementById("monthSelect").value;

	console.log(yearSelect + "年 " + monthSelect + "月");

	/** @RequestMapping("/api")が付与されたクラスの@GetMapping("reservationLost")が付与されたメソッドを呼び出す */
	let url = "http://localhost:8080/api/reservationList?year=" + yearSelect + "&month=" + monthSelect;

	/** XMLHttpRequestオブジェクトを作成 */
	let request = new XMLHttpRequest();

	/** リクエストを開始する HTTPメソッド, リクエスト先のURL, 非同期でリクエストを送信する = true  */
	request.open("GET", url, true);

	/** リクエストヘッダにJSON形式でデータを送信する */
	request.setRequestHeader("Content-Type", "application/json");

	/** readyStatusプロパティが変化した時に呼び出される関数 */
	request.onreadystatechange = function() {

		/** readyState は0～4まであり通信の進捗を示す 4 = サーバーからの応答をすべて受信したという意味
		 *  status=200はリクエストが成功し正常にサーバのレスポンスが返されたという意味
		 */
		if (request.readyState === 4 && request.status === 200) {
			/** JSON形式のデータをJavaScriptオブジェクトに変換する ここでは予約可能な時間のデータが返されている*/
			let reservationList = JSON.parse(request.responseText);

			/** 予約リストのtableの中身を削除するメソッド */
			deleteReservationList();

			/** 予約リストのtableを動的に作成するメソッド */
			populateReservationList(reservationList);
		}
	};

	/** XMLHttpRequestを送信する */
	request.send();

}

function deleteReservationList() {
	console.log("deleteReservationList()");
	const table = document.getElementById("reservationList");


	while (table.firstChild) {
		table.removeChild(table.firstChild);
	}


}

/** 予約リストのtableを動的に作成するメソッド */
function populateReservationList(reservationList) {
	console.log("populateReservationList()");
	const table = document.getElementById("reservationList");
	const tbody = table.appendChild(document.createElement("tbody"));

	/** テーブルのヘッダーを作成 */
	const headerRow = document.createElement("tr");
	const headers = ["No.", "ID", "名前", "予約日", "予約時間", "備考"];
	headers.forEach(header => {
		const th = document.createElement("th");
		th.textContent = header;
		headerRow.appendChild(th);
	});
	tbody.appendChild(headerRow);

	let i = 1;

	/** テーブルの行を取得できた予約分作成する */
	reservationList.forEach(e => {
		const tr = document.createElement("tr");

		/** No. */
		const num = document.createElement("td");
		num.textContent = i;
		tr.appendChild(num);

		/** id */
		const id = document.createElement("td");
		id.textContent = e.id;
		tr.appendChild(id);

		/** 名前 */
		const name = document.createElement("td");
		name.textContent = e.user_id.username;
		tr.appendChild(name);

		/** 予約日 */
		const reserveDate = document.createElement("td");
		reserveDate.textContent = e.reserveDate;
		tr.appendChild(reserveDate);

		/** 予約時間 */
		const reserveTime = document.createElement("td");
		reserveTime.textContent = e.reserveTime;
		tr.appendChild(reserveTime);

		/** 備考 */
		const remarks = document.createElement("td");
		remarks.textContent = e.remarks;
		tr.appendChild(remarks);

		tr.classList.add("border");
		tbody.appendChild(tr);

		i++;
	});

}

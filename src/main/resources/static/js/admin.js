/** 現在の年月を設定する */
window.onload = function setCurrentDate() {
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



/** 年月を条件に予約リストを取得して、table要素を作成する関数 */
function getReservationsByYearAndMonth() {
	/** yearとmonthのセレクトボタンの値を取得する */
	let yearSelect = document.getElementById("yearSelect").value;
	let monthSelect = document.getElementById("monthSelect").value;

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

			/** 予約リストのtableの中身を削除する関数 */
			deleteReservationList();

			/** 予約リストのtableを動的に作成する関数 */
			createReservationList(reservationList);

		}
	};

	/** XMLHttpRequestを送信する */
	request.send();

}

/** 予約リストテーブルの中身を削除する関数 */
function deleteReservationList() {
	const table = document.getElementById("reservationList");


	/** tableの最初の子要素を削除する処理のループ */
	while (table.firstChild) {
		table.removeChild(table.firstChild);
	}


}

/** 予約リストのtableを動的に作成する関数 */
function createReservationList(reservationList) {
	const table = document.getElementById("reservationList");
	const tbody = table.appendChild(document.createElement("tbody"));


	/** テーブルの行を取得できた予約分作成する */
	if (reservationList != null) {
		/** pageパラメータを付与する関数 */
		addPageParameter();

		const rowsPerPage = 1; // 1ページに表示する列数
		const params = new URLSearchParams(location.search); // URLのパラメータを取得
		const currentPage = params.get("page"); // 現在のpageパラメータを取得
		const start = (currentPage - 1) * rowsPerPage; // 該当ページの開始地点
		const end = start + rowsPerPage; // 該当ページの終了地点
		const currentPageData = reservationList.slice(start, end); // 該当ページの予約情報をstart,endで制限

		for (let i = 0; i <= currentPageData.length - 1; i++) {
			const tr = document.createElement("tr");

			/** 予約日 */
			const reserveDate = document.createElement("td");
			reserveDate.textContent = formatDates(currentPageData[i].reserveDate) + "  " + formatTimes(currentPageData[i].reserveTime);
			tr.appendChild(reserveDate);

			/** 名前 */
			const name = document.createElement("td");
			name.textContent = currentPageData[i].user_id.username + " 様";
			tr.appendChild(name);

			/** 詳細 */
			const details = document.createElement("td");
			const a = document.createElement("a");
			a.textContent = "詳細を確認";
			a.classList.add("btn", "btn-primary", "text-center", "w-100");
			a.href = "/admin/details?user_id=" + currentPageData[i].user_id.id;
			details.appendChild(a);
			tr.appendChild(details);

			tr.classList.add("border");
			tbody.appendChild(tr);
		}

		/** 予約の数に応じてページネーションを作成する関数 */
		createReservationPagenation(reservationList, rowsPerPage);


	}

}

/** pageパラメータを付与する関数 */
function addPageParameter() {
	/** 現在のURLを取得 */
	const url = new URL(window.location.href);

	// URLSearchParamsオブジェクトを取得
	const params = url.searchParams;

	if (!params.has('page')) {
		const newUrl = url + "?page=1";
		window.history.replaceState("", "", newUrl);
	}
}

/** 日付を1/11 （水）形式に変換する関数 */
function formatDates(reserveDate) {
	const split = reserveDate.split("-");
	const year = split[0];
	const month = split[1];
	const date = split[2];

	const dateObject = new Date(year, month, date);
	const weekArr = ["日", "月", "火", "水", "木", "金", "土"];
	const dayOfWeek = dateObject.getDay();

	const formattedDate = month + "/" + date + "(" + weekArr[dayOfWeek] + ")";
	return formattedDate;
}

/** 時間をHH:mmの形式に変換する関数 */
function formatTimes(reserveTime) {
	const split = reserveTime.split(":");
	const hour = split[0];
	const minute = split[1];

	const formattedTime = hour + ":" + minute;

	return formattedTime;
}

/** 予約の数に応じてページネーションを作成する関数 */
function createReservationPagenation(reservationList, rowsPerPage) {
	const params = new URLSearchParams(location.search); // 現在のURLのパラメータ
	const currentPage = params.get("page"); // pageパラメータの値

	const reservationCount = reservationList.length; // 予約の数
	const pageCount = Math.ceil(reservationCount / rowsPerPage); // 作成するページ数
	const pagenation = document.getElementById("pagenation");
	pagenation.innerHTML = ""; // 空にする

	if (reservationList.length != 0) {
		/** 現在いるページ数の時はspan 他のページ数の時はaタグで該当ページに遷移 */
		for (let i = 1; i <= pageCount; i++) {
			if (i == currentPage) {
				const span = document.createElement("span");
				span.textContent = i;
				pagenation.appendChild(span);
			} else {
				const a = document.createElement("a");
				a.textContent = i;
				a.href = "/?page=" + i;
				pagenation.appendChild(a);
			}
		}
	} else {
		/** 予約がない場合は、「予約がありません」と表示 */
		const container = document.getElementById("noDataMessage");
		container.innerHTML = "";
		const noDataMessage = document.createElement("h3");
		noDataMessage.textContent = "予約がありません";
		noDataMessage.classList.add("text-center");
		container.appendChild(noDataMessage);
	}
}



/**
 * 予約日を取得し、/api/availableTimesでエンドポイントにリクエストを送信する
 */
function getAvailableTimes() {
	/** reserveDateというIDをもった要素を取得 */
	let selectedDate = document.getElementById("reserveDate").value;

	/** @RequestMapping("/api")が付与されたクラスの@GetMapping("availableTimes")が付与されたメソッドを呼び出す */
	let url = "http://localhost:8080/api/availableTimes?date=" + selectedDate;

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
			let availableTimes = JSON.parse(request.responseText);

			/** 予約可能な時間を動的に生成する関数 */
			populateAvailableTimes(availableTimes);
		}
	};

	/** XMLHttpRequestを送信する */
	request.send();
}

/**
 * 引数に渡された"availableTimes"配列から、HTMLのセレクトボックスに予約可能な時間を動的に生成する
 */
function populateAvailableTimes(availableTimes) {
	/** defaultIDをもった要素を取得して削除する */
	let defaultDateSelected = document.getElementById("default");

	/** defaultDateSelectedが取得できた時、要素を削除する
	 *  selectボタンを１度も選択していないときにのみ取得できる
	 */
	if (defaultDateSelected != null) {
		defaultDateSelected.remove();
	}


	/** reserveTimeフィールドをもった要素を取得して空要素を入れる */
	let reserveTimeSelect = document.getElementById("reserveTime");
	reserveTimeSelect.innerHTML = "";

	/** availableTimeの配列の中身をoption要素を作成して表示する */
	for (let i = 0; i < availableTimes.length; i++) {

		/** option要素を作成する */
		let option = document.createElement("option");

		/** Moment.jsで日付フォーマット 引数はMoment.jsが認識できるISO 8601形式で*/
		let formattedTime = moment("1970-01-01T" + availableTimes[i]).format("HH:mm");
		option.value = formattedTime;
		option.text = formattedTime;

		/** option要素をセレクトボタンの子要素に追加 */
		reserveTimeSelect.appendChild(option);
	}
}
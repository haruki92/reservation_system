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

    /** 予約可能な 時間 分 それぞれの配列を作成する*/
    let availableHours = [];
    let availableMinutes = [];

    for (let i = 0; i < availableTimes.length; i++) {
        /** : の文字列で分割して配列にする (例) 10:20:00 の場合 timeArr = [10, 20, 00] */
        const timeArr = availableTimes[i].split(":");

        const hour = timeArr[0];
        const minute = timeArr[1];

        /** hourが既に配列に存在する場合はpushしない */
        if (!availableHours.includes(hour + ":00:00")) {
            availableHours.push(hour + ":00:00");
        }

        availableMinutes.push(hour + ":" + minute + ":00");

    }

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

    console.log(availableHours);


    /** availableTimeの配列の中身をoption要素を作成して表示する */
    for (let i = 0; i < availableHours.length; i++) {
        /** optgroup要素を作成する */
        let optgroup = document.createElement("optgroup");

        const hoursArr = availableHours[i].split(":");

        /** Moment.jsで日付フォーマット 引数はMoment.jsが認識できるISO 8601形式で */
        let formattedHours = moment("1970-01-01T" + availableHours[i]).format("HH:mm");

        optgroup.label = formattedHours + " ～";

        /** optgroup要素をセレクトボタンの子要素に追加 */
        reserveTimeSelect.appendChild(optgroup);


        /** optgroup要素の中に予約可能な時間のoption要素を作成する */
        for (let j = 0; j < availableMinutes.length; j++) {
            const minutesArr = availableMinutes[j].split(":");

            /** hoursArrにminutesArrの値が含まれている場合 = 時(h)が一致する時 にのみoption要素を作成する */
            if (hoursArr[0].includes(minutesArr[0])) {
                let option = document.createElement("option");
                /** Moment.jsで日付フォーマット 引数はMoment.jsが認識できるISO 8601形式で */
                let formattedMinutes = moment("1970-01-01T" + availableMinutes[j]).format("HH:mm");

                option.value = formattedMinutes;
                option.text = formattedMinutes;
                /** option要素をoptgroup要素の子要素に追加 */
                optgroup.appendChild(option);
            }
        }


    }
}
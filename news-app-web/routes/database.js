var mysql_config = {
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'news_app',
    port: 3306,
    timezone: 'UTC',
};
var mysql = require('mysql');
// 使用连接池，提升性能
var poolConn = mysql.createPool(mysql_config);

//连接池查询
var querypoll = function (sql, params, callback) {
    poolConn.getConnection(function (err, conn) {
        if (err) {
            callback(err, null, null);
        } else {
            var query = conn.query(sql, params, function (qerr, vals, fields) {
                //释放连接
                conn.release();
                //事件驱动回调
                callback(qerr, vals, fields);
            });
            console.log("sql:::" + query.sql);
        }
    });
};


//不使用连接池
var query = function (sql, params, callback) {
    var conn = mysql.createConnection(mysql_config);
    //连接错误，2秒重试
    conn.connect(function (err) {
        if (err) {
            console.log("error when connecting to db:", err);
            setTimeout(query, 2000);
        } else {
            var query = conn.query(sql, params, function (qerr, vals, fields) {
                //关闭连接
                conn.end();
                //事件驱动回调
                callback(qerr, vals, fields);
            });
            console.log("sql:::" + query.sql);
        }
    });


    conn.on("error", function (err) {
        console.log("db error", err);
        // 如果是连接断开，自动重新连接
        if (err.code === "PROTOCOL_CONNECTION_LOST") {
            query();
        } else {
            throw err;
        }
    });
}


module.exports = {
    querypoll: querypoll,
    query: query
};
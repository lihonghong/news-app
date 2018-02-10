var express = require('express');
var router = express.Router();

router.get('/', function (req, res) {
    conn.query('select * from news_hotquery where TO_DAYS(create_time) = TO_DAYS(NOW()) order by position', function (error, rows) {
        var data = [];
        for (var i = 0; i < rows.length; i++) {
            data.push({
                name: rows[i].name,
                type: rows[i].type,
                source: rows[i].source,
                category: rows[i].category,
                position: rows[i].position,
                create_time: rows[i].create_time,
                update_time: rows[i].update_time,
                valid: rows[i].valid
            });
        }

        var result = {
            data: data
        }

        return res.json(result);
    });
});

module.exports = router;

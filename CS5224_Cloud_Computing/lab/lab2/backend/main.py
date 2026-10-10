import os
from flask import Flask, render_template
from google.cloud import bigquery

app = Flask(__name__)

bq_client = bigquery.Client()

@app.get("/")
def index():
    return render_template("index.html")

@app.get("/user/<string:uid>")
def hello(uid):
    if uid.isdigit():
        try:
            sql_query = f"""
               SELECT * FROM `cs5224-lab2-511110.twitter.twitter_ids`
               WHERE uid="{uid}"
            """
            job = bq_client.query(sql_query)
            rows = job.result()
            if rows.total_rows == 0:
                return "ID not found!", 200
            for row in rows:
                return {
                    "uid": row["uid"],
                    "followee_count": row["followee_count"],
                    "follower_count": row["follower_count"]
                }, 200
        except Exception as e:
            return "sad", 500
    return "sad", 500


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))

from pyspark.sql import SparkSession, functions as F

PROJECT_ID = "cs5224-lab2-511110"
COLLECTION_NAME = "twitter_ids"

spark = SparkSession.builder.appName("twitter_data").getOrCreate()

gcs_path = "gs://lab2-e1337597/data/twitter_combined.txt"
gcs_path2 = "gs://lab2-e1337597/data/results"

df = spark.read.text(gcs_path)

df_split = df.withColumn("info", F.split(F.col("value"), " "))

split_col = df_split["info"]

df = df_split.withColumn("follower", split_col.getItem(0))
df = df.withColumn("followee", split_col.getItem(1))["follower","followee"]

uids = df.select("follower").union(df.select("followee")).distinct().withColumnRenamed("follower", "uid")

follower_counts = df.groupBy("followee").agg(F.countDistinct("follower").alias("follower_count")).withColumnRenamed("followee", "uid")
followee_counts = df.groupBy("follower").agg(F.countDistinct("followee").alias("followee_count")).withColumnRenamed("follower", "uid")
uids = (
    uids.join(followee_counts, on="uid", how="left")
    .join(follower_counts, on="uid", how="left")
    .fillna({"follower_count": 0, "followee_count": 0 })
)
uids.write.format("bigquery").option("project", PROJECT_ID).mode("overwrite").save("twitter.twitter_ids")

spark.stop()

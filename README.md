# Ad-events-Impressions-Analysis-Apache-Spark-Scala-

The file starting with assets_ contains asset (image) impressions. Each line represents 1 asset impression.
The file starting ad-events_ contains various kinds of ad events. Each line represents one ad event. We are
interested only in two kinds of ad events - 'view' and 'click'. Rest of the ad events should be ignored. Type of
ad event is specified in the value of the field 'e'. You can see "e":"view" and "e":"click" in the json messages.

Each line also has another id called 'page view id'. This id is specified by json key 'pv'. You can find json key
values similar to "pv":"7963ee21-0d09-4924-b315-ced4adad425f" in both the files.

The aim is to join the data in two files using "pv". You need to parse these files and combine the data in two
files to check how many asset impressions, views and clicks are present in both the files for each page view id.

The output file should have four columns: page view id, asset impressions, views and clicks
7963ee21-0d09-4924-b315-ced4adad425f 3 2 0
6933ee21-0d09-9898-b315-ced4adad890o 5 0 0
9963ee21-0d09-6955-b315-ced4adad5697 3 2 1

You need to ignore all the rows with asset impressions = 0. It's possible that some page view ids will have
views and clicks but may not have any asset impressions.


#set document(
  title: [CS52224 Cloud Computing Lab 2]
)
#set page(
  paper:"a4",
  flipped: false,
  margin: 2.54cm
)

#align(horizon + center)[
#title()
Done by: Jess Kwek (A0292071J)
]
#pagebreak()

= Part 1

*a) Design a cloud strategy for a healthcare analytics SaaS used by multiple hospitals.
Patient data must be tightly governed, demand can spike during outbreaks, and the sys-
tem must still scale nationally. What deployment model(s) and scaling architecture(s)
would you choose, and why?*

For the deployment model I would choose to use the Hybrid Cloud model. This allows the hospitals to store their data on the private cloud using an onsite private cloud while running the analytics application on the public cloud directly. Hospitals can then secure patient data according to their needs while being able to leverage on the flexibility of the public cloud to scale their application according to current demand.

For the scaling architecture I would choose to scale the application using horizontal scaling. This is because vertical scaling might cause the service to go down during outbreaks as the server may have to restart after the scaling to utilise the additional resources. This would not be ideal during busy periods. Horizontal scaling allows the original server to continue serving requests while the new instance is being spun up. With a proper load balancer that distributes the requests to each instance of the server properly, an auto scaling configuration that would scale the infrastructure according to current demands, the application will be able to scale nationally to meet the increased request volumes during outbreaks!

However this will mean that the servers serving patient data to the application must also have sufficient capacity to handle the spikes in demand. This is a tradeoff that will occur as patient data needs to be tightly governed and it is more secure to host this onsite to have a better control of where the data is sent to and stored.


*b) A bank wants to move away from its legacy application and modernise it with strict
compliance and some sensitive data. Which cloud service and cloud deployment model
would you use and why?*

I would use IaaS as the cloud service as it gives the most amount of control of the underlying resources such as the operating system, applications and virtual resources to the bank. This can be used in combination with the onsite private cloud to also provide the bank with control over the hardware and infrastructure that will run the application and store the sensitive data in. With the amount of control, the bank can configure the server to comply with their corresponding compliance and policies. These policies can include, access control, data encryption, networking and how data should be stored.

On top of that, the bank will also be able to store sensitive data locally and control how this data is being used in order to protect this data from abuse. Also, the bank can port over features of the legacy application slowly and test them in that environment before replacing that function of the legacy application with the modernised alternative. This allows the bank to stage their replacement at their own pace and test their application locally to ensure the functionality meets their expectations.

However this also means that the bank will have to be responsible for the infrastructure maintenance backups and disaster recovery which is a fair tradeoff considering the strict compliance that the bank has to meet

= Part 2

== Front-End

I hosted the website on Cloud Run to serve a static webpage for the user to search for a UID at https://\<cloud run url\>/

Upon submitting the form, the website will send a GET request at with the requested uid to retrieve the data and display the response

=== Screenshots

#figure(
  image("images/user1.png"),
  caption: [User 214328887]
)

#figure(
  image("images/user2.png"),
  caption: [User 107830991]
)

== Back-End

I hosted the backend on Cloud Run which serves an api at /user/\<uid\> where uid is the uid submitted by the user on the frontend.

To retrieve the counts, the backend will query the BigQuery database which contains the counts for all uids in the provided dataset which was inserted by the data analysis component after processing it via Managed Apache on GCP.

The backend runs on flask and python

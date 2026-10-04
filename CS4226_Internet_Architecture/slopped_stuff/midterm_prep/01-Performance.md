# Chapter 1 — Performance

[Practice index](README.md) · [Reference](../quick_references/01-Performance.md)

**4 MCQs + 4 fill-in questions.** Choose one answer per MCQ. For numerical blanks, use exact answers or at least 3 significant figures.

## MCQs

### P1. Capacity and queueing

A 20 Mbps link carries a long-run average of 12 Mbps of useful traffic. Ignore overhead; all transmitted bits are useful. Which statement is correct?

- A. Throughput is 20 Mbps, and utilization is 60%.
- B. Throughput is 12 Mbps, utilization is 60%, and bursts can still cause queueing.
- C. Throughput is 12 Mbps, and a mean load below capacity guarantees zero queueing.
- D. Utilization is 12%, because 12 Mbps is carried.

Answer: ______

### P2. Store-and-forward delay

A 1,500-byte packet crosses three store-and-forward links of 12, 6 and 3 Mbps. Their lengths are 200, 100 and 300 km; propagation speed is $2\times10^8$ m/s. Each of the two intermediate routers adds 0.2 ms processing and 0.5 ms queueing delay. Ignore endpoint processing.

What is the time from the first bit starting transmission at the source to the last bit arriving at the destination?

- A. 8.4 ms
- B. 10.0 ms
- C. 7.0 ms
- D. 11.4 ms

Answer: ______

### P3. Can bandwidth meet the deadline?

A 1,000-byte packet crosses a 400 km link at propagation speed $2\times10^8$ m/s. Ignore processing and queueing. Which statement about a target of 1.5 ms from transmission start to complete reception is correct?

- A. It is impossible at any link rate because propagation alone takes 2 ms.
- B. It is achievable at a rate of $8{,}000/0.0015$ bits/s.
- C. It is achievable if transmission delay is reduced to 1 ms.
- D. It is achievable at any rate above 1 Mbps.

Answer: ______

### P4. Overload probability

A 6 Mbps link admits five users. Each independently needs 2 Mbps with probability 0.2 at a randomly chosen instant, and zero otherwise. What is the probability that aggregate demand **exceeds** capacity?

- A. 0.05792
- B. 0.00032
- C. 0.00672
- D. 0.2

Answer: ______

## Fill in the blanks

### P5. General single-server system

A stable, work-conserving single server admits 120 jobs/s. Its mean waiting population is 18 and mean service time is 5 ms. Arrival and service distributions are otherwise unspecified.

- (a) Utilization: $\rho=$ ______.
- (b) Mean queueing delay: $\bar D=$ ______ ms.
- (c) Mean sojourn time: $\bar W=$ ______ ms.
- (d) Mean total population: $\bar L=$ ______ jobs.
- (e) These calculations require exponential service times: ______ (**true/false**).

### P6. Mixed packet lengths

A stable, work-conserving link has capacity 100,000 bytes/s. Stream A has 300 packets/s: 25% are 100 bytes and 75% are 200 bytes. Stream B has 500 packets/s, all 50 bytes. The mean total population is 40 packets. No exponential service assumption is made.

- (a) Mean size of an A packet: ______ bytes.
- (b) Mean size of an arbitrary arriving packet: ______ bytes.
- (c) Mean service time: ______ ms.
- (d) Utilization: ______.
- (e) Mean sojourn time: ______ ms.
- (f) Mean queueing delay: ______ ms.

### P7. Admission control

A stable service receives 250 attempted requests/s and immediately rejects 20% before entry. Admitted requests spend 0.08 s on average in the service; all admitted requests complete.

- (a) Arrival rate for Little's law applied to the admitted-request subsystem: ______ requests/s.
- (b) Mean population of that subsystem: ______ requests.
- (c) Completed-request throughput: ______ requests/s.

### P8. Complete the identities

For a stable, work-conserving single server, fill in the symbolic expressions. Here $\lambda>0$, $\rho$ is utilization and $\bar L$ is the mean total population.

- (a) Mean waiting population: $\bar Q=$ ______.
- (b) Mean queueing delay: $\bar D=$ ______.
- (c) If the server is busy with probability $\rho$, the mean number **in service** is ______.
- (d) Little's law requires Poisson arrivals: ______ (**true/false**).

---

[Answer key — open after attempting](solutions/01-Performance.md)

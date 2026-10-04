# Chapter 2 — Queueing Models

[Practice index](README.md) · [Reference](../quick_references/02-Queueing.md)

**6 MCQs + 6 fill-in questions.** Choose one answer per MCQ. $L$ includes service; $Q$ counts waiting only. Give numerical blanks exactly or to at least 3 significant figures.

## MCQs

### Q1. Independence

Roll two independent fair dice. Let $A$ mean “the sum is 7” and $B$ mean “the second die is 3”. Which statement is correct?

- A. They are dependent because both involve the second die.
- B. They are mutually exclusive.
- C. They are independent because $P(A\cap B)=1/36=P(A)P(B)$.
- D. They are independent because $P(A\cap B)=0$.

Answer: ______

### Q2. Bayes' rule

A packet is malformed with probability 0.02. A detector flags 95% of malformed packets and 5% of well-formed packets. Given that it flags a packet, what is the probability the packet is malformed?

- A. 0.95
- B. $19/68\approx0.2794$
- C. 0.019
- D. 0.02

Answer: ______

### Q3. Competing Poisson processes

Independent buses A and B arrive at rates 6/hour and 4/hour. You arrive independently of both processes and board whichever comes first. Which pair gives **(mean waiting time, probability of boarding A)**?

- A. (10 minutes, 0.6)
- B. (6 minutes, 0.4)
- C. (25 minutes, 0.5)
- D. (6 minutes, 0.6)

Answer: ______

### Q4. Geometric memorylessness

Independent trials succeed with probability 0.25. Let $X$ count trials **including** the first success. What is $P(X>7\mid X>3)$?

- A. $(0.75)^4$
- B. $(0.75)^7$
- C. $(0.25)^4$
- D. $1-(0.75)^4$

Answer: ______

### Q5. Empty queue versus empty system

A stationary M/M/1 queue has $\lambda=8$ packets/s and $\mu=10$ packets/s. What is the probability that its **waiting queue** is empty?

- A. 0.20
- B. 0.36
- C. 0.64
- D. 0.80

Answer: ______

### Q6. Network stability

Which condition is required for a stationary open Jackson network with single exponential servers?

- A. Total service capacity must exceed external arrivals; no node-by-node check is needed.
- B. Every node must have equal arrival and service rates.
- C. Each node's total effective arrival rate, including feedback, must be strictly below its service rate.
- D. Only the first node must have arrival rate below service rate.

Answer: ______

## Fill in the blanks

### Q7. Conditional and unconditional sojourn times

A FIFO M/M/1 queue has $\lambda=3$ jobs/s and $\mu=5$ jobs/s. It is stationary.

- (a) Conditional mean sojourn time when an arrival sees an empty system: ______ s.
- (b) Conditional mean when the waiting queue is empty but the server is busy: ______ s.
- (c) Conditional mean when three other jobs are waiting and the server is busy: ______ s.
- (d) Unconditional mean sojourn time of a typical arrival: ______ s.
- (e) Property explaining why a busy server's residual service still has mean $1/\mu$: ______.

### Q8. Effective bandwidth

An M/M/1 link has $\mu=100$ packets/s. It must meet all three conditions: utilization at most 0.8, mean sojourn time at most 40 ms, and mean queueing delay at most 20 ms.

- (a) Maximum arrival rate from the utilization constraint alone: ______ packets/s.
- (b) Maximum arrival rate from the sojourn constraint alone: ______ packets/s.
- (c) Maximum arrival rate from the queueing-delay constraint alone: ______ packets/s.
- (d) Maximum arrival rate satisfying all constraints: ______ packets/s.
- (e) At the rate in (d), mean sojourn time is ______ ms.
- (f) These constraints guarantee every packet's queueing delay is at most 20 ms: ______ (**true/false**).

### Q9. Pooling and equal-delay splitting

Two independent Poisson flows have rates 4 and 2 packets/s. Under the course's capacity-partition model, they share a pooled M/M/1 link of 10 packets/s, or use two separate M/M/1 queues with capacities $\mu_1+\mu_2=10$.

- (a) Pooled mean sojourn time: ______ s.
- (b) Pooled mean queueing delay: ______ s.
- (c) To equalize the separate queues' mean sojourn times, use $(\mu_1,\mu_2)=$ (______, ______) packets/s.
- (d) The resulting common mean sojourn time is ______ s.
- (e) Pooling any two stable queues under this model always gives a mean sojourn time smaller than either separate queue: ______ (**true/false**).

### Q10. Jackson network with feedback

External Poisson arrivals enter server 1 at 2 jobs/s. Every completion at server 1 goes to server 2. After server 2, a job exits with probability $1/2$, otherwise visits server 3. Every completion at server 3 returns to server 2. Routing choices are independent. Servers are FIFO with independent exponential service rates $(4,6,5)$ jobs/s. Assume stationary operation.

- (a) Effective rates $(\lambda_1,\lambda_2,\lambda_3)=$ (______, ______, ______) jobs/s.
- (b) Mean total network population: ______ jobs.
- (c) Mean time in the network per external job: ______ s.
- (d) Mean number of visits to server 2 per external job: ______.
- (e) $P(L_1=1,L_2=2,L_3=0)=$ ______.

### Q11. Joint queue events

External Poisson arrivals at 2 jobs/s visit server 1 then server 2. After server 2 they exit with probability $1/2$, otherwise rejoin server 2. Independent exponential service rates are $\mu_1=4$, $\mu_2=8$ jobs/s; routing choices are independent. Assume stationary operation of this Jackson network.

- (a) Effective arrival rate at server 2: ______ jobs/s.
- (b) Probability both **systems** are empty: ______.
- (c) Probability both **waiting queues** are empty: ______.
- (d) Probability the two total system populations are equal, $P(L_1=L_2)$: ______.
- (e) Probability the two waiting populations are equal, $P(Q_1=Q_2)$: ______.

### Q12. Formula and concept completion

Fill in each independent blank.

- (a) For $0<\rho<1$, $\sum_{n=0}^{\infty}n\rho^n=$ ______.
- (b) A stationary M/M/1 queue has mean waiting population $\bar Q=$ ______, expressed in terms of $\rho$.
- (c) If $T\sim\mathrm{Exp}(\alpha)$, then $P(T>s+t\mid T>s)=$ ______, for $s,t\ge0$.
- (d) Burke's theorem says a stationary M/M/1 queue's departure process is ______ with rate ______, where the arrival rate is $\lambda$ and service rate is $\mu>\lambda$.
- (e) Splitting one M/M/1 queue with rates $(\lambda,\mu)$ into $k$ identical queues with rates $(\lambda/k,\mu/k)$ multiplies mean sojourn time by ______.
- (f) In M/D/1, the letter D means ______ service times.

---

[Answer key — open after attempting](solutions/02-Queueing.md)

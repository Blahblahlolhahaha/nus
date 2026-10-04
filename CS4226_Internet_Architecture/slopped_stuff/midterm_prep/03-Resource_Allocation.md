# Chapter 3 — Resource Allocation

[Practice index](README.md) · [Reference](../quick_references/03-Resource_Allocation.md)

**4 MCQs + 4 fill-in questions.** Choose one answer per MCQ. All fairness problems use fixed routes. Rates and capacities in R2–R6 use Mbps. A bottleneck must be saturated and have a largest (normalized, if weighted) rate for the flow concerned.

## MCQs

### R1. Utility curves

Which description best matches the lecture's **hard real-time** utility curve as bandwidth increases?

- A. Utility is highest at zero bandwidth and falls thereafter.
- B. Utility increases linearly at all bandwidths without a threshold.
- C. Utility must be exactly the same as for an elastic file transfer.
- D. Utility is very low below a required threshold and rises sharply once it is met.

Answer: ______

### R2. One link, unequal demands

One link of capacity 14 serves four equal-weight flows with demands $(1,3,8,10)$. What is the max-min fair allocation?

- A. $(1,3,5,5)$
- B. $(3.5,3.5,3.5,3.5)$
- C. $(1,3,3,7)$
- D. $(1,3,4,6)$

Answer: ______

### R3. Weighted allocation with caps

One link has capacity 18, demands $(2,8,9,10)$ and weights $(1,2,1,2)$. Which is the weighted max-min fair allocation?

- A. $(3,6,3,6)$
- B. $(2,6,3,6)$
- C. $(2,6.4,3.2,6.4)$
- D. $(2,8,4,4)$

Answer: ______

### R4. Does saturation prove fairness?

Three equal-weight flows have unlimited demand. Flow 1 uses links A and B, flow 2 uses A only, and flow 3 uses B only. Capacities are $C_A=6$, $C_B=9$. A proposed allocation is $(2,4,7)$.

Which statement is correct?

- A. It is max-min fair because both links are saturated.
- B. It is not max-min fair: changing it to $(3,3,6)$ increases the smallest flow while decreasing only larger flows.
- C. It is infeasible because flow 1 consumes capacity on two links.
- D. It is max-min fair because flow 3 has the largest rate.

Answer: ______

## Fill in the blanks

### R5. Network water filling

Three flows have demands $(5,2,6)$. Flows 1 and 2 each use links A and B. Flow 3 uses links B and C. Capacities are $C_A=6$, $C_B=10$, $C_C=7$. All weights are equal.

- (a) The max-min fair allocation is $(x_1,x_2,x_3)=$ (______, ______, ______) Mbps.
- (b) Link loads $(\text{load}_A,\text{load}_B,\text{load}_C)=$ (______, ______, ______) Mbps.
- (c) All bottleneck links for flow 1: ______ (**list letters**).
- (d) All bottleneck links for flow 2: ______ (**list letters, or “none”**).
- (e) All bottleneck links for flow 3: ______ (**list letters**).

### R6. Weighted network water filling

Use the same topology, demands and capacities as R5, now with weights $(2,1,1)$.

- (a) The weighted max-min allocation is $(x_1,x_2,x_3)=$ (______, ______, ______) Mbps.
- (b) The normalized rates $(x_1/\phi_1,x_2/\phi_2,x_3/\phi_3)=$ (______, ______, ______).
- (c) All weighted bottleneck links for flow 1: ______.
- (d) All weighted bottleneck links for flow 2: ______.
- (e) All weighted bottleneck links for flow 3: ______.
- (f) Multiplying every weight by 7 changes the allocation: ______ (**true/false**).

### R7. Minimum-delay capacity split

External Poisson arrivals at 2 jobs/s visit server 1 and then server 2. After every service at server 2, a job exits with probability $1/4$, otherwise rejoins server 2. Services are independent exponentials, routing choices are independent, and total capacity is $\mu_1+\mu_2=19$ jobs/s.

Let $x=\mu_1$ and minimize stationary mean end-to-end sojourn time.

- (a) Effective arrival rates $(\lambda_1,\lambda_2)=$ (______, ______) jobs/s.
- (b) The feasible stable interval for $x$ is ______ $<x<$ ______ jobs/s.
- (c) Complete the objective: $f(x)=1/(x-2)+$ ______.
- (d) The optimal capacities are $(\mu_1^*,\mu_2^*)=$ (______, ______) jobs/s.
- (e) The minimum mean end-to-end time is ______ s.
- (f) The optimal **spare-capacity** ratio $(\mu_1^*-\lambda_1):(\mu_2^*-\lambda_2)$ is ______.

### R8. Concept completion

- (a) In weighted max-min fairness, compare the rates ______ rather than raw rates $x_i$, where $\phi_i$ is flow $i$'s weight.
- (b) Under ordinary max-min fairness, increasing any flow within the feasible domain must require decreasing another flow whose original rate is ______ (**smaller than or equal to / strictly larger than**) the increased flow's original rate.
- (c) For finite demands, every flow in a max-min allocation must either have a bottleneck resource or have ______.
- (d) A demand-satisfied flow can still have a bottleneck link: ______ (**true/false**).
- (e) GPS is a fluid sharing ideal, while ______ is a packet scheduling approach mentioned in the lecture (**abbreviation**).
- (f) Equalizing M/M/1 sojourn times across separate queues means equalizing $\mu_i-$ ______.

---

[Answer key — open after attempting](solutions/03-Resource_Allocation.md)

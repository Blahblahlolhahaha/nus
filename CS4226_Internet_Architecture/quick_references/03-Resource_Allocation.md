# Chapter 3: Resource Allocation

[Reference index, notation and formula lookup](README.md)

## Resource allocation and fairness

Sources: Resource Allocation lecture; Tutorial 4 Q1/Q2; Assignment Q2.

### Why allocate resources?

The Internet's best-effort service does not guarantee delivery or delay. Elastic applications tolerate varying throughput/delay more readily than real-time voice/video. Utility $U_i$ expresses how performance benefits user $i$.

| Utility shape shown in lecture | Interpretation |
|---|---|
| Elastic: increasing with diminishing gains | More bandwidth helps, with diminishing marginal benefit |
| Hard real-time: threshold/step | Little usefulness below the required resource level |
| Delay-adaptive: S-shaped | Benefit improves sharply around a useful operating region |
| Rate-adaptive: increasing, initially S-shaped | Quality improves as the application can use higher rates |

These are qualitative curves, not prescribed utility formulas. Max-min fairness is an allocation criterion; it does not generally maximize total throughput or total utility.

### Feasibility and the fairness definition

For link-flow incidence $A_{ri}=1$ when flow $i$ uses link $r$:

$$
0\le x_i\le d_i,\qquad \sum_i A_{ri}x_i\le C_r\quad\text{for every link }r.
$$

The **same end-to-end rate** $x_i$ consumes capacity on every link in its route.

An allocation $\mathbf x$ is **max-min fair** if any feasible increase in a flow's rate requires decreasing a flow whose existing rate is smaller or equal:

$$
y_i>x_i\ \Longrightarrow\ \exists j:\ y_j<x_j\le x_i
\quad\text{for every feasible alternative }\mathbf y.
$$

Intuition: satisfy small demands, then share what remains equally among unsatisfied flows. For fixed routes, positive capacities and finite demand caps in this model, progressive filling produces the unique max-min rate allocation. Demand caps can also be viewed as private bottleneck resources.

### Bottleneck test

A link $r$ is a bottleneck **for flow $i$** iff:

1. Flow $i$ uses $r$.
2. The link is saturated: $\sum_{j:A_{rj}=1}x_j=C_r$.
3. Flow $i$ has a largest rate on that link: $x_i\ge x_j$ for every flow $j$ using it.

With infinite demands, an allocation is max-min fair iff every flow has a bottleneck. With finite demands, every flow must either reach its demand or have a bottleneck. A demand-satisfied flow **can still have** bottleneck links if the test holds (Tutorial 4 Q2).

**Saturated is necessary but not sufficient:** a saturated link need not bottleneck every flow crossing it.

### Weighted max-min fairness

Use positive weights $\phi_i$ and compare **normalized rates** $x_i/\phi_i$. Unsatisfied flows sharing a limiting resource receive rates in proportion to their weights.

For one link:

$$
\boxed{x_i=\min(d_i,\phi_i\theta)},\qquad
\sum_i\min(d_i,\phi_i\theta)=\min\left(C,\sum_i d_i\right).
$$

When no demand cap binds, $x_i=C\phi_i/\sum_j\phi_j$. If a demand binds, freeze that flow and redistribute its unused share. Setting every $\phi_i=1$ recovers ordinary max-min fairness.

The weighted bottleneck test replaces condition 3 with:

$$
\frac{x_i}{\phi_i}\ge\frac{x_j}{\phi_j}\quad\text{for all flows }j\text{ on the saturated link}.
$$

### Progressive water filling: reliable calculation method

Start with all $x_i=0$ and all flows active. For ordinary fairness set $\phi_i=1$.

At each iteration, compute the increment in normalized level:

$$
\boxed{\Delta=\min\left\{
\min_{i\in\mathcal A}\frac{d_i-x_i}{\phi_i},\quad
\min_{r:\sum_{i\in\mathcal A}A_{ri}\phi_i>0}
\frac{C_r-\sum_jA_{rj}x_j}{\sum_{i\in\mathcal A}A_{ri}\phi_i}
\right\}}.
$$

1. Increase each active flow by $\phi_i\Delta$.
2. Freeze flows that reach demand **and all active flows using any newly saturated link**.
3. Recompute residual capacities and repeat until no active flows remain.
4. Check feasibility, then identify **all** bottlenecks using final normalized rates.

Frozen flows still consume capacity. Handle simultaneous demand/link events together. Network flows can freeze at different levels, so a single global $\theta$ is not enough for a multi-link network.

### Worked allocation patterns

**Lecture, one resource:** $C=10$, demands $(1,4,5,6)$ → $(1,3,3,3)$.

**Lecture, weighted resource:** $C=16$, demands $(4,2,10,4)$, weights $(2.5,4,0.5,1)$ → $(4,2,6,4)$. Cap small demands and redistribute until all remaining capacity is used.

**Lecture, three links:** constraints $x_1+x_2\le3$, $x_1+x_2+x_3\le6$, $x_1+x_3\le7$; demands $(2,4,4)$.

- Ordinary fairness: $(1.5,1.5,3)$.
- Weights $(2,1,2)$: $(2,1,3)$.

**Tutorial 4 Q1 / Assignment Q2 topology:**

$$
x_1+x_2\le5,\qquad x_1+x_2+x_3\le8,\qquad x_3\le5,
\qquad\mathbf d=(4,2,4).
$$

| Weights | Final allocation | Bottleneck links by flow |
|---|---|---|
| $(1,1,1)$ | $(3,2,3)$ | Flow 1: $C_1,C_2$; flow 2: demand-satisfied, no link bottleneck; flow 3: $C_2$ |
| $(2,1,1)$ | $(10/3,5/3,3)$ | Flows 1 and 2: $C_1$; flow 3: $C_2$ |
| $(1,2,2)$ | $(2,2,4)$ | Flows 1 and 3: $C_2$; flow 2: demand-satisfied, no link bottleneck |

For $(2,1,1)$, $C_1$ saturates at normalized level $5/3$, fixing flows 1 and 2. Flow 3 then uses the remaining $8-5=3$ units on $C_2$.

**Tutorial 4 Q2 topology:** $x_1+x_2\le2$, $x_1+x_3\le4$, $x_3+x_4\le5$, $x_2+x_4\le3$; demands $(1,4,3,2)$.

| Weights | Allocation | Bottlenecks |
|---|---|---|
| All 1 | $(1,1,3,2)$ | Flows 1,2: $C_1$; flow 3: $C_2,C_3$; flow 4: $C_4$ |
| $(2,3,4,5)$ | $(7/8,9/8,3,15/8)$ | Flow 1: $C_1$; flows 2,4: $C_4$; flow 3: demand-satisfied |

In the weighted case, $C_4$ saturates first at normalized level $3/(3+5)=3/8$. Freeze flows 2 and 4; flow 1 rises to $2-9/8=7/8$, and flow 3 eventually reaches 3. Final $C_2,C_3$ are unsaturated.

Implementation concept from the lecture: distributed routers need local packet scheduling to approximate fair sharing. GPS (Generalized Processor Sharing) is the fluid ideal; WFQ (Weighted Fair Queueing) is a packet scheduling approach. Detailed SDN mechanisms belong to the next lecture.

## Optimize capacity for minimum mean delay

Source: Tutorial 4 Q3. This objective differs from equalizing flow delays or max-min fairness.

### Tutorial's two-server derivation

External rate $\lambda$ visits server 1 once and server 2 an average of 4 times (exit probability $1/4$). Thus effective rates are $(\lambda,4\lambda)$.

With total capacity $M=\mu_1+\mu_2$, stability is possible iff $M>5\lambda$, with individual requirements $\mu_1>\lambda$, $\mu_2>4\lambda$.

Set $x=\mu_1$:

$$
f(x)=\frac1{x-\lambda}+\frac4{M-x-4\lambda},\qquad
\lambda<x<M-4\lambda.
$$

$$
f'(x)=-\frac1{(x-\lambda)^2}+\frac4{(M-x-4\lambda)^2}=0
\quad\Rightarrow\quad M-x-4\lambda=2(x-\lambda).
$$

$$
\boxed{\mu_1^*=\frac{M-2\lambda}{3},\qquad
\mu_2^*=\frac{2M+2\lambda}{3}},\qquad
\boxed{\bar W_{\min}=\frac9{M-5\lambda}}.
$$

$$
f''(x)=\frac2{(x-\lambda)^3}+\frac8{(M-x-4\lambda)^3}>0,
$$

so the stationary point is the unique minimum. The **spare capacities** have ratio $1:2$, not the total capacities and not the arrival rates.

### Useful derived generalization: square-root spare-capacity rule

For fixed loads $\lambda_i$, positive coefficients $a_i$, and total capacity $\sum_i\mu_i=M$, minimize

$$
F=\sum_i\frac{a_i}{\mu_i-\lambda_i},\qquad
H=M-\sum_i\lambda_i>0.
$$

Let $s_i=\mu_i-\lambda_i>0$. Differentiating $\sum_i a_i/s_i+\eta(\sum_i s_i-H)$ gives $-a_i/s_i^2+\eta=0$. Therefore:

$$
\boxed{s_i^*=H\frac{\sqrt{a_i}}{\sum_j\sqrt{a_j}}},\qquad
\boxed{\mu_i^*=\lambda_i+s_i^*},\qquad
\boxed{F_{\min}=\frac{(\sum_i\sqrt{a_i})^2}{H}}.
$$

For a Jackson network's mean end-to-end delay, $a_i=v_i=\lambda_i/\Lambda$. For separated streams' packet-weighted average delay, $a_i=\lambda_i/\sum_j\lambda_j$. State the objective before choosing the coefficients. Extra capacity caps or unequal capacity costs require adjusting this unconstrained allocation rule.

---

[Chapter 1: Network Performance](01-Performance.md) | [Chapter 2: Queueing Models](02-Queueing.md) | [Chapter 3: Resource Allocation](03-Resource_Allocation.md)

# Resource Allocation — Answer Key

[Questions](../03-Resource_Allocation.md) · [Practice index](../README.md)

## MCQs

| Question | Answer | Explanation |
|---|---|---|
| R1 | **D** | Hard real-time utility has a required threshold; useful service depends on meeting it. |
| R2 | **A** | Satisfy demands 1 and 3, then split the remaining 10 equally. B violates demands; C and D favor a larger flow while a smaller unsatisfied flow can increase. |
| R3 | **C** | Uncapped shares $(3,6,3,6)$ violate flow 1's demand. Cap it at 2; split the remaining 16 in weights $2:1:2$, giving $(6.4,3.2,6.4)$. |
| R4 | **B** | At $(2,4,7)$ neither saturated link bottlenecks flow 1, since it is not a largest-rate user. Moving to $(3,3,6)$ increases flow 1 while decreasing only flows originally larger than it. |

## R5

- (a) **$(4,2,4)$ Mbps**.
- (b) **$(6,10,4)$ Mbps**.
- (c) **A and B**.
- (d) **None**.
- (e) **B**.

First fill all flows to 2, when flow 2 reaches demand. Loads then are $(4,6,2)$. Increase flows 1 and 3 together by 2; A and B saturate simultaneously, giving $(4,2,4)$.

Flow 1 is a largest-rate user of both A and B. Flow 2 is demand-satisfied but smaller than flow 1 on A and flows 1/3 on B, so neither is its bottleneck. Flow 3 is tied for largest on B. C is unsaturated.

## R6

- (a) **$(4,2,4)$ Mbps**.
- (b) **$(2,2,4)$**.
- (c) **A**.
- (d) **A**.
- (e) **B**.
- (f) **False**.

Increase active rates as $(2\theta,\theta,\theta)$. At $\theta=2$, A saturates and flow 2 reaches demand simultaneously. Freeze flows 1 and 2 at $(4,2)$; flow 3 rises from 2 to 4 until B saturates.

Although the allocation happens to equal R5, the bottleneck assignments differ. On A the normalized rates are both 2; on B they are $(2,2,4)$, so only flow 3 has a largest normalized rate there. This also demonstrates that a demand-satisfied flow (flow 2) can have a bottleneck. Multiplying all weights by a common positive factor preserves relative weights and allocations.

## R7

- (a) **$(2,8)$ jobs/s**.
- (b) **$2<x<11$ jobs/s**.
- (c) **$4/(11-x)$** (equivalently $4/(19-x-8)$).
- (d) **$(5,14)$ jobs/s**.
- (e) **1 s**.
- (f) **$1:2$** (equivalently $3:6$).

Effective rate at node 2 is $2/(1/4)=8$ and mean visit count is 4. Thus

$$
f(x)=\frac1{x-2}+\frac4{11-x},\qquad
f'(x)=-\frac1{(x-2)^2}+\frac4{(11-x)^2}.
$$

Within the stable interval both denominators are positive. Setting $f'(x)=0$ gives $11-x=2(x-2)$, hence $x=5$. Also

$$
f''(x)=\frac2{(x-2)^3}+\frac8{(11-x)^3}>0.
$$

The optimum is $(5,14)$, with delay $1/3+4/6=1$ s and spare capacities $(3,6)$. Allocating total capacity in a $1:2$ ratio would confuse total and spare capacity.

## R8

- (a) **$x_i/\phi_i$**.
- (b) **Smaller than or equal to**.
- (c) **Reached its demand** (equivalently $x_i=d_i$).
- (d) **True**.
- (e) **WFQ** (Weighted Fair Queueing).
- (f) **$\lambda_i$**.

The finite-demand condition allows a satisfied flow to stop even without a saturated link. GPS is Generalized Processor Sharing, a fluid ideal. Equal sojourn times satisfy $1/(\mu_i-\lambda_i)=1/(\mu_j-\lambda_j)$, so spare capacities must be equal; this is a different objective from weighted fairness or minimizing total mean delay.

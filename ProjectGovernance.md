Simal is a consensus-based community project. Our governance model is inspired by that used within [The Apache Software Foundation](http://www.apache.org/) projects. Anyone with an interest in Simal can contribute to the project's design, documentation and strategy. Anyone can join the project community and participate in the decision-making process.

# Community Roles #

## Users ##

Users are community members who have a need for Simal. Anyone can be a user, there are no special requirements. Users are the most important members of our community, without them the project is pointless. We ask that users participate in the project as much as possible, in particular we encourage them to:

  * improve documentation (by asking for clarifications on existing docs)
  * participate on the email lists (users are often the best people to support new users)
  * report bugs
  * identify requirements
  * evangelise about the project
  * anything else useful to the project as a whole

## Contributors ##

Contributors are community members who are relatively new to Simal. They make valuable contributions to the project, as discussed above, but have not yet earned full commit access to project resources. Therefore, contributions to project resources are through email discussion and via patches attached to issues in our issue tracker.

Anyone can become a contributor, all you need to do is one or more of the following:

  * participate on the email lists
  * provide documentation
  * report bugs
  * fix bugs
  * identify requirements
  * add features
  * evangelise about the project
  * anything else useful to the project as a whole

## Committers ##

Committers are community members who have shown that they are committed to the continued development of Simal. This can either be through constructive contribution to project requirements, design discussion, documentation, evangalism, coding or any other valuable contribution. Committers are given commit access to the project resources.

Anyone can become a committer. Typically they will need to show they have an understanding for Simal, its objectives and its strategy and have provided valuable contributions to the project. Once the existing committers feel that an individual has earned committership they will call a vote to accept the new committer.

## Project Management Committee ##

The Simal Project Management Committee (PMC) consists of those individuals identified as "Project owners" on the development site. The PMC has additional responsibilities to ensure the smooth running of the project (see above). Membership of the PMC is by invitation only and must receive consensus approval of the current PMC members.

**FIXME (rdg) - Need to define these additional responsibilities**

# Decision-making #

Decisions about the future of the project are made through discussion with all members of the community, from the newest user to the most experienced developer. All discussion takes place on the simal-contributors mailing list. In order to prompt a discussion about a new development idea, send an email to the simal-contributors list with an appropriate tag in the subject line. For example, if you are looking for general discussion, use `[RT]` for "Random Thought". If the discussion (or your own thinking) has reached the point of a substantive proposal, use `[Proposal]` in your subject line.

Usually, gaining consensus within the community is easy since all community members have a common set of interests. You will find that most day-to-day operations do not require explicit voting. So long as no one explicitly opposes a proposal then it is recognised as having gained consensus.

Sometimes a proposal will attract little or no comment from the community. This is not an indication of a lack of interest or support. In fact, it is the exact opposite of this, it is an implicit support for the idea being expressed. This is the concept of "Lazy Consensus". Lazy Consensus means that if no-one speaks, then no-one objects.

For lazy consensus to be effective it is necessary to allow at least 72 hours before assuming that there are no objections to your plans. This is to ensure that everyone is given enough time to read, digest and respond to your email.

Not all decisions can be made using lazy consensus, a few must gain explicit approval in the form of a vote. If a formal vote on a proposal is called (signalled simply by sending a email with `[Vote]` in the subject line), then the following procedure applies. All participants on the simal-contributors list may express an opinion and vote. Express your opinion thus:

  * +1 "yes", "agree" - also willing to help bring about the proposed action
  * +0 "yes", "agree" - not willing or able to help bring about the proposed action
  * -0 "no", "disagree" - but will not oppose the action going forward
  * -1 "no", "disagree" - opposes the action going forward and must propose an alternate action to address the issue (or a justification for not addressing the issue)

When a `[Vote]` receives a "-1", it is the responsibility of the community to continue discussion until that vote is either rescinded or the proposal itself is altered in order to achieve consensus (possibly by withdrawing it altogether). Where consensus cannot be achieved, the Simal Project Management Committee will decide the forward course of action.

Every member of the community, from an interested user, through to the most active developer has a vote. We encourage all members of the community to show their support or otherwise with a vote and, where necessary, continued discussion. However, only committers in the project (as defined above) have binding votes for the purposes of decision making. It is the responsibility of the committers to ensure all voices are heard in a vote, so whilst users do not have a binding vote, a well supported -1 from a non-committer should be considerd.

## Types of Approval ##

Different actions require different types of approval:

| **Type** | **Description** | **Duration** |
|:---------|:----------------|:-------------|
| Lazy consensus |  An action with lazy consensus is implicitly allowed unless a binding -1 vote is received, at which time, depending on the type of action, a vote will be called. Note that even though a binding -1 is required to prevent the action all community members are encouraged to cast a -1 vote with supporting argument. Committers are expected to evaluate the argument and, if necessary, support it with a binding -1. | 72 hours |
| Lazy majority |  A lazy majority vote requires more binding +1 votes than binding -1 votes. | 72 hours |
| Consensus approval |  Consensus approval requires 3 binding +1 votes and no binding -1 votes. | 72 hours |
| Unanimous consensus |  All of the binding votes that are cast are to be +1 and there can be no binding vetoes (-1). | 120 hours |
| 2/3 majority |  Some strategic actions require a 2/3 majority of PMC members, in addition, 2/3 of the binding votes cast must be +1. Such actions typically affect the foundation of the project (e.g. adopting a new codebase to replace an existing product). | 120 hours |


**Note:**

**Although only binding votes are counted it is important that all interested parties vote. This allows committers to gauge the feeling of the community as a whole and vote accordingly.**

## When is a vote required? ##

This section describes some of the actions that will require a vote, all other actions are assumed to require lazy consensus, that is no vote is required unless a binding -1 is cast. In the event of an undefined action requiring a vote the type of vote shall be determined by the PMC through lazy consensus.

| Action | Description | Approval Type |
|:-------|:------------|:--------------|
| Release Plan | Defines the timetable and actions for a release. A release plan cannot be vetoed (hence lazy majority). | Lazy Majority |
| Product Release | When a release of one of the project's products is ready, a vote is required to accept the release as an official release of the project. A release cannot be vetoed (hence lazy majority). | Lazy Majority |
| New Committer | A new committer has been proposed. | Consensus Approval |
| New PMC member | A new PMC member has been proposed. | Consensus Approval |
| Committer Removal | When removal of commit privileges is sought. | Unanimous consensus |
| PMC Member Removal | When removal of PMC membership is sought. | Unanimous consensus |

# Review #

The governance model of the Simal project is itself open to review and revision by the Simal community
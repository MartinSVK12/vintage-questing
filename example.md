In `afterGameStart` method in your mods main class:
```java
ItemRewardTemplate reward = new ItemRewardTemplate("vintagequesting:reward1",new ItemStack(Item.basket));
ClickTaskData clickTask = new ClickTaskData("vintagequesting:click1");
RetrievalTaskData retrievalTask = new RetrievalTaskData("vintagequesting:retrieval1",new ItemStack(Block.netherrackIgneous,32));
RetrievalTaskData retrievalTask2 = new RetrievalTaskData("vintagequesting:retrieval2",new ItemStack(Block.blockDiamond,16)).setConsume();
CraftingTaskData craftingTask = new CraftingTaskData("vintagequesting:crafting1",new ItemStack(Block.workbench,8));
KillTaskData killTask = new KillTaskData("vintagequesting:kill1", EntityPig.class,4);
QuestTemplate quest = new QuestTemplate("vintagequesting:test","quest.vq.test","quest.vq.test",Block.netherrackIgneous,Logic.AND,Logic.AND).setTasks(listOf(clickTask,retrievalTask,retrievalTask2,craftingTask,killTask)).setRewards(listOf(reward));
ChapterTemplate chapter = new ChapterTemplate("vintagequesting:test",Block.dirt,"chapter.vq.test","chapter.vq.test",listOf(quest));

ItemRewardTemplate reward2 = new ItemRewardTemplate("vintagequesting:reward2",new ItemStack(Block.torchRedstoneIdle));
ClickTaskData clickTask2 = new ClickTaskData("vintagequesting:click2");
RetrievalTaskData retrievalTask3 = new RetrievalTaskData("vintagequesting:retrieval3",new ItemStack(Block.slabBrickStonePolished,32));
RetrievalTaskData retrievalTask4 = new RetrievalTaskData("vintagequesting:retrieval4",new ItemStack(Block.trommelIdle,16)).setConsume();
CraftingTaskData craftingTask2 = new CraftingTaskData("vintagequesting:crafting2",new ItemStack(Block.furnaceBlastIdle,8));
KillTaskData killTask2 = new KillTaskData("vintagequesting:kill2", EntitySlime.class,4);
QuestTemplate quest2 = new QuestTemplate("vintagequesting:test2","quest.vq.test2","quest.vq.test2",Block.bedrock,Logic.AND,Logic.AND)
    .setTasks(listOf(clickTask2,retrievalTask3,retrievalTask4,craftingTask2,killTask2))
    .setRewards(listOf(reward2))
    .setPreRequisites(listOf(quest))
    .setX(64);
ChapterTemplate chapter2 = new ChapterTemplate("vintagequesting:test2",Block.blockDiamond,"chapter.vq.test2","chapter.vq.test2",listOf(quest,quest2));
```
In your `.lang` file:
```mclang
chapter.vq.test.name=Test Chapter
chapter.vq.test.desc=This is a description for a quest chapter!
quest.vq.test.name=Test Quest
quest.vq.test.desc=This is a testing quest!
chapter.vq.test2.name=Test Chapter 2
chapter.vq.test2.desc=This is a description for a second quest chapter!
quest.vq.test2.name=Test Quest 2
quest.vq.test2.desc=This is a second testing quest!
```

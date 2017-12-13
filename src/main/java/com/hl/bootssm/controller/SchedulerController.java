package com.hl.bootssm.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hl.bootssm.annotation.TimeTaskAnnotation;
import com.hl.bootssm.components.SpringUtil;
import com.hl.bootssm.config.MessCodeConstant;
import com.hl.bootssm.domain.QuartzBizInfo;
import com.hl.bootssm.domain.ResultDo;
import com.hl.bootssm.utils.DateTools;
import com.hl.bootssm.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * 定时任务
 *
 * @author Static
 */
@Slf4j
@Controller
@RequestMapping("schedule")
public class SchedulerController {
    @Value("${quartz.enabled}")
    private Boolean quartzEnabled;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("quartzEnabled", quartzEnabled);
        if (quartzEnabled) {
            try {
                SchedulerFactoryBean schedulerFactoryBean = SpringUtil.getBean(SchedulerFactoryBean.class);
                Scheduler startQuertzObject = schedulerFactoryBean.getObject();
                model.addAttribute("isStarted", startQuertzObject.isStarted());
                model.addAttribute("isInStandbyMode", startQuertzObject.isInStandbyMode());
                if (startQuertzObject.isShutdown()) {
                    return "schedule/index";
                }
                GroupMatcher groupMatcher = GroupMatcher.groupContains(Scheduler.DEFAULT_GROUP);
                Set<TriggerKey> triggerKeys = startQuertzObject.getTriggerKeys(groupMatcher);
                TreeMap<String, QuartzBizInfo> quartzBizPojos = Maps.newTreeMap();
                for (TriggerKey triggerKey : triggerKeys) {
                    Trigger trigger = startQuertzObject.getTrigger(triggerKey);
                    if (!(trigger instanceof CronTriggerImpl)) {
                        continue;
                    }
                    CronTriggerImpl cronTrigger = (CronTriggerImpl) trigger;
                    ResultDo<QuartzBizInfo> resultDo = this.loadJob(startQuertzObject, cronTrigger);
                    if (resultDo.isSuccess()) {
                        QuartzBizInfo quartzBizPojo = resultDo.getResult();
                        quartzBizPojos.put(quartzBizPojo.getTriggerName(), quartzBizPojo);
                    } else {
                        QuartzBizInfo quartzBizPojo = new QuartzBizInfo();
                        quartzBizPojo.setJobName(cronTrigger.getJobName());
                        quartzBizPojo.setErrorMsg(resultDo.getErrorDescription());
                        quartzBizPojos.put(quartzBizPojo.getJobName(), quartzBizPojo);
                    }
                }
                if (!quartzBizPojos.isEmpty()) {
                    List<Object> tasks = Lists.newArrayList();
                    for (String key : quartzBizPojos.keySet()) {
                        tasks.add(quartzBizPojos.get(key));
                    }
                    model.addAttribute("quartzBizPojos", tasks);
                }
            } catch (Exception e) {
                log.error("显示定时任务失败！", e);
            }
        }
        return "schedule/index";
    }

    /**
     * 加载单个任务信息
     *
     * @param startQuertzObject
     * @param trigger
     * @return
     */
    private ResultDo<QuartzBizInfo> loadJob(Scheduler startQuertzObject, CronTriggerImpl trigger) {
        JobKey jobKey = trigger.getJobKey();
        try {
            JobDetail jobDetail = startQuertzObject.getJobDetail(trigger.getJobKey());
            QuartzBizInfo quartzBizPojo = new QuartzBizInfo();
            quartzBizPojo.setJobName(trigger.getJobName());
            quartzBizPojo.setHasJob(true);
            quartzBizPojo.setJobDescription(jobDetail.getDescription());
            quartzBizPojo.setCronEx(trigger.getCronExpression());
            quartzBizPojo.setTriggerName(trigger.getName());
            quartzBizPojo.setHasTrigger(true);
            quartzBizPojo.setJobClass(jobDetail.getJobClass().getName());
            if (trigger.getPreviousFireTime() != null) {
                quartzBizPojo.setLastFireTime(DateTools.dateToString(trigger.getPreviousFireTime(), DateTools.TIME_PATTERN));
            } else {
                quartzBizPojo.setLastFireTime("");
            }
            if (trigger.getNextFireTime() != null) {
                quartzBizPojo.setNextFireTime(DateTools.dateToString(trigger.getNextFireTime(), DateTools.TIME_PATTERN));
            } else {
                quartzBizPojo.setNextFireTime("");
            }
            Trigger.TriggerState state = startQuertzObject.getTriggerState(trigger.getKey());
            quartzBizPojo.setTriggerState(state.name());
            ResultDo<QuartzBizInfo> resultDo = ResultDo.build();
            resultDo.setResult(quartzBizPojo);
            return resultDo;
        } catch (SchedulerException e) {
            log.error("获取定时任务失败! jobKey={}", jobKey.getName(), e);
            return ResultDo.build(MessCodeConstant.ERROR_SYSTEM).setErrorDescription(e.getMessage());
        }
    }

//    /**
//     * 同步任务
//     *
//     * @param
//     */
//    @RequestMapping(value = "/syncJob", method = RequestMethod.POST)
//    @RequiresPermissions("schedule:syncJob")
//    @LogInfoAnnotation(moduleName = "定时任务", desc = "同步任务")
//    @ResponseBody
//    public ResultDo syncJob(Model model) {
//        try {
//            syncJobWithDict();
//        } catch (Exception e) {
//            return ResultDo.build(MessCodeConstant.ERROR_SYSTEM).setErrorDescription(e.getMessage());
//        }
//        return ResultDo.build();
//    }

    /**
     * 根据字典表配置同步任务到quartz
     *
     * @throws Exception
     */
//    private void syncJobWithDict() throws Exception {
//        List<SysDictPojo> sysDictPojos = sysDictComponent.findByGroupName("timeTaskDefaultGroup");
//        SchedulerFactoryBean schedulerFactoryBean = SpringUtil.getBean(SchedulerFactoryBean.class);
//        Scheduler startQuertzObject = schedulerFactoryBean.getObject();
//        List<TriggerKey> triggerKeys = Lists.newArrayList();
//        List<JobKey> jobKeys = Lists.newArrayList();
//        // 同步字典表配置
//        for (SysDictPojo sysDictPojo : sysDictPojos) {
//            try {
//                Class jobClass = Class.forName(sysDictPojo.getKey());
//                TimeTaskAnnotation timeTaskAnnotation = (TimeTaskAnnotation) jobClass.getAnnotation(TimeTaskAnnotation.class);
//                String jobName = timeTaskAnnotation.jobName();
//                String jobDesc = timeTaskAnnotation.jobDesc();
//                String triggleName = timeTaskAnnotation.triggerName();
//                JobKey jobKey = JobKey.jobKey(jobName, Scheduler.DEFAULT_GROUP);
//                jobKeys.add(jobKey);
//                TriggerKey triggerKey = TriggerKey.triggerKey(triggleName, Scheduler.DEFAULT_GROUP);
//                triggerKeys.add(triggerKey);
//                boolean existsTriggle = startQuertzObject.checkExists(triggerKey);
//                if (existsTriggle) {
//                    CronTriggerImpl trigger = (CronTriggerImpl) startQuertzObject.getTrigger(triggerKey);
//                    // 同步cron表达式即可
//                    if (!trigger.getCronExpression().equals(sysDictPojo.getValue())) {
//                        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
//                        triggerBuilder.withIdentity(triggleName, Scheduler.DEFAULT_GROUP);
//                        triggerBuilder.startNow();
//                        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(sysDictPojo.getValue()));
//                        CronTriggerImpl newTrigger = (CronTriggerImpl) triggerBuilder.build();
//                        // 修改一个任务的触发时间
//                        startQuertzObject.rescheduleJob(triggerKey, newTrigger);
//                    }
//                } else {
//                    // 新增任务
//                    boolean existsJob = startQuertzObject.checkExists(jobKey);
//                    // 新增triggle
//                    TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
//                    triggerBuilder.withIdentity(triggleName, Scheduler.DEFAULT_GROUP);
//                    triggerBuilder.startNow();
//                    triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(sysDictPojo.getValue()));
//                    CronTriggerImpl newTrigger = (CronTriggerImpl) triggerBuilder.build();
//                    if (!existsJob) {
//                        // 新增job
//                        JobDetail jobDetail = JobBuilder.newJob(jobClass)
//                                .withIdentity(jobKey)
//                                .withDescription(jobDesc)
//                                .build();
//                        startQuertzObject.scheduleJob(jobDetail, newTrigger);
//                    } else {
//                        // 修改一个任务的触发时间
//                        startQuertzObject.rescheduleJob(triggerKey, newTrigger);
//                    }
//                }
//            } catch (ClassNotFoundException e) {
//                log.error("定时任务找不到实体类! class={}", sysDictPojo.getKey(), e);
//                // 删除无效的字典
//                sysDictComponent.deleteLogical(sysDictPojo.getId());
//            }
//        }
//        cleanAll(startQuertzObject, jobKeys, triggerKeys);
//    }
//
//    /**
//     * 清除残留数据，保证与字典表数据一致
//     *
//     * @throws SchedulerException
//     */
//    private void cleanAll(Scheduler scheduler, List<JobKey> newJobKeys, List<TriggerKey> newTriggerKeys) throws Exception {
//        GroupMatcher groupMatcher = GroupMatcher.groupContains(Scheduler.DEFAULT_GROUP);
//        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(groupMatcher);
//        // 过滤残留的trigger
//        for (TriggerKey triggerKey : triggerKeys) {
//            Optional optional = newTriggerKeys.stream().filter(item -> item.getName().equals(triggerKey.getName())).findFirst();
//            if (!optional.isPresent() && scheduler.checkExists(triggerKey)) {
//                // 删除不存在于配置的trigger
//                CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
//                scheduler.pauseTrigger(triggerKey);// 停止触发器
//                scheduler.unscheduleJob(triggerKey);// 移除触发器
//                scheduler.deleteJob(trigger.getJobKey());// 删除任务
//                continue;
//            }
//        }
//        // 过滤残留的job
//        Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
//        for (JobKey jobKey : jobKeys) {
//            Optional optional = newJobKeys.stream().filter(item -> item.getName().equals(jobKey.getName())).findFirst();
//            if (!optional.isPresent() && scheduler.checkExists(jobKey)) {
//                // 删除不存在于配置的jobKey
//                scheduler.deleteJob(jobKey);// 删除任务
//                continue;
//            }
//        }
//    }

    /**
     * 添加任务
     *
     * @param
     */
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String delTask(Model model,
                          @RequestParam(value = "jobClass", required = false) String jobClass,
                          @RequestParam(value = "cron", required = false) String cron) {
        if (StringTools.isBlank(jobClass)) {
            model.addAttribute("operType", "addJob");
        } else {
            model.addAttribute("operType", "editJob");
        }
        model.addAttribute("jobClass", jobClass);
        model.addAttribute("cron", cron);
        return "schedule/form";
    }

    /**
     * 保存任务
     *
     * @param
     */
    @RequestMapping(value = "/saveTask", method = RequestMethod.POST)
    public String saveTask(@RequestParam(value = "jobClass", required = true) String jobClassName,
                           @RequestParam(value = "cron", required = true) String cron) {
        try {
            if (StringTools.isBlank(jobClassName) || StringTools.isBlank(cron)) {
                return "redirect:/schedule/index";
            }
            Class jobClass = Class.forName(jobClassName);
            TimeTaskAnnotation timeTaskAnnotation = (TimeTaskAnnotation) jobClass.getAnnotation(TimeTaskAnnotation.class);
            SchedulerFactoryBean schedulerFactoryBean = SpringUtil.getBean(SchedulerFactoryBean.class);
            Scheduler startQuertzObject = schedulerFactoryBean.getObject();
            JobKey jobKey = JobKey.jobKey(timeTaskAnnotation.jobName(), Scheduler.DEFAULT_GROUP);
            if (startQuertzObject.checkExists(jobKey)) {
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                triggerBuilder.withIdentity(timeTaskAnnotation.triggerName(), Scheduler.DEFAULT_GROUP);
                triggerBuilder.startNow();
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                CronTriggerImpl newTrigger = (CronTriggerImpl) triggerBuilder.build();
                // 修改一个任务的触发时间
                startQuertzObject.rescheduleJob(TriggerKey.triggerKey(timeTaskAnnotation.triggerName(), Scheduler.DEFAULT_GROUP), newTrigger);
                return "redirect:/schedule/index";
            }
            // 新增triggle
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(timeTaskAnnotation.triggerName(), Scheduler.DEFAULT_GROUP);
            triggerBuilder.startNow();
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            CronTriggerImpl newTrigger = (CronTriggerImpl) triggerBuilder.build();
            // 新增job
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .withDescription(timeTaskAnnotation.jobDesc())
                    .build();
            startQuertzObject.scheduleJob(jobDetail, newTrigger);
        } catch (Exception e) {
            log.error("保存任务失败！jobClass={}, cron={}", jobClassName, cron, e);
        }
        return "redirect:/schedule/index";
    }

    /**
     * 删除任务
     *
     * @param
     */
    @RequestMapping(value = "/delTask", method = RequestMethod.POST)
    @ResponseBody
    public ResultDo delTask(@RequestParam(value = "jobName", required = true) String jobName) {
        SchedulerFactoryBean schedulerFactoryBean = SpringUtil.getBean(SchedulerFactoryBean.class);
        Scheduler startQuertzObject = schedulerFactoryBean.getObject();
        JobKey jobKey = new JobKey(jobName, Scheduler.DEFAULT_GROUP);
        try {
            startQuertzObject.deleteJob(jobKey);
            return ResultDo.build();
        } catch (SchedulerException e) {
            log.error("删除任务失败！jobName={}", jobName, e);
            return ResultDo.build(MessCodeConstant.ERROR_SYSTEM).setErrorDescription(e.getMessage());
        }
    }

    /**
     * 手动执行
     *
     * @param
     */
    @RequestMapping(value = "/doTask", method = RequestMethod.POST)
    @ResponseBody
    public ResultDo doTask(@RequestParam(value = "jobName", required = true) String jobName) {
        SchedulerFactoryBean schedulerFactoryBean = SpringUtil.getBean(SchedulerFactoryBean.class);
        Scheduler startQuertzObject = schedulerFactoryBean.getObject();
        JobKey jobKey = new JobKey(jobName, Scheduler.DEFAULT_GROUP);
        try {
            startQuertzObject.triggerJob(jobKey);
            return ResultDo.build();
        } catch (SchedulerException e) {
            log.error("手动执行失败！jobName={}", jobName, e);
            return ResultDo.build(MessCodeConstant.ERROR_SYSTEM).setErrorDescription(e.getMessage());
        }
    }

    /**
     * 暂停任务
     *
     * @param
     */
    @RequestMapping(value = "/pauseTask", method = RequestMethod.POST)
    @ResponseBody
    public ResultDo pauseTask(@RequestParam(value = "triggerKey", required = true) String triggerKey) {
        SchedulerFactoryBean schedulerFactoryBean = SpringUtil.getBean(SchedulerFactoryBean.class);
        Scheduler startQuertzObject = schedulerFactoryBean.getObject();
        try {
            TriggerKey key = new TriggerKey(triggerKey, Scheduler.DEFAULT_GROUP);
            startQuertzObject.pauseTrigger(key);
            return ResultDo.build();
        } catch (SchedulerException e) {
            log.error("暂停任务失败！triggerKey={}", triggerKey, e);
            return ResultDo.build(MessCodeConstant.ERROR_SYSTEM).setErrorDescription(e.getMessage());
        }
    }

    /**
     * 恢复任务
     *
     * @param
     */
    @RequestMapping(value = "/resumeTask", method = RequestMethod.POST)
    @ResponseBody
    public ResultDo resumeTask(@RequestParam(value = "triggerKey", required = true) String triggerKey) {
        SchedulerFactoryBean schedulerFactoryBean = SpringUtil.getBean(SchedulerFactoryBean.class);
        Scheduler startQuertzObject = schedulerFactoryBean.getObject();
        try {
            TriggerKey key = new TriggerKey(triggerKey, Scheduler.DEFAULT_GROUP);
            startQuertzObject.resumeTrigger(key);
            return ResultDo.build();
        } catch (SchedulerException e) {
            log.error("恢复任务失败！triggerKey={}", triggerKey, e);
            return ResultDo.build(MessCodeConstant.ERROR_SYSTEM).setErrorDescription(e.getMessage());
        }
    }

//    /**
//     * 临时停止任务
//     *
//     * @param
//     */
//    @RequestMapping(value = "/close", method = RequestMethod.POST)
//    //@RequiresPermissions("schedule:close")
//    @LogInfoAnnotation(moduleName = "定时任务", desc = "临时停止任务")
//    @ResponseBody
//    public ResultDo close() {
//        SchedulerFactoryBean schedulerFactoryBean = SpringUtil.getBean(SchedulerFactoryBean.class);
//        Scheduler startQuertzObject = schedulerFactoryBean.getObject();
//        try {
//            if (!startQuertzObject.isInStandbyMode()) {
//                //startQuertzObject.pauseAll();
//                startQuertzObject.standby();
//            }
//            return ResultDo.build();
//        } catch (SchedulerException e) {
//            log.error("临时停止任务失败！", e);
//            return ResultDo.build(MessCodeConstant.ERROR_SYSTEM).setErrorDescription(e.getMessage());
//        }
//    }

//    /**
//     * 运行实例
//     *
//     * @param
//     */
//    @RequestMapping(value = "/start", method = RequestMethod.POST)
//    //@RequiresPermissions("schedule:start")
//    @LogInfoAnnotation(moduleName = "定时任务", desc = "运行实例")
//    @ResponseBody
//    public ResultDo start() {
//        SchedulerFactoryBean schedulerFactoryBean = SpringUtil.getBean(SchedulerFactoryBean.class);
//        Scheduler startQuertzObject = schedulerFactoryBean.getObject();
//        try {
//            if (startQuertzObject.isInStandbyMode()) {
//                startQuertzObject.start();
//            }
//            return ResultDo.build();
//        } catch (SchedulerException e) {
//            log.error("运行实例失败！", e);
//            return ResultDo.build(MessCodeConstant.ERROR_SYSTEM).setErrorDescription(e.getMessage());
//        }
//    }

//    /**
//     * 删除实例
//     *
//     * @param
//     */
//    @RequestMapping(value = "/destory", method = RequestMethod.POST)
//    //@RequiresPermissions("schedule:destory")
//    @LogInfoAnnotation(moduleName = "定时任务", desc = "删除实例")
//    @ResponseBody
//    public ResultDo destory() {
//        SchedulerFactoryBean schedulerFactoryBean = SpringUtil.getBean(SchedulerFactoryBean.class);
//        Scheduler startQuertzObject = schedulerFactoryBean.getObject();
//        try {
//            if (!startQuertzObject.isShutdown()) {
//                startQuertzObject.shutdown(true);
//            }
//            return ResultDo.build();
//        } catch (SchedulerException e) {
//            log.error("关闭实例失败！", e);
//            return ResultDo.build(MessCodeConstant.ERROR_SYSTEM).setErrorDescription(e.getMessage());
//        }
//    }

}

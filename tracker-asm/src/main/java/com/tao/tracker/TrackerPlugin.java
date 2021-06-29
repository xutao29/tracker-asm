package com.tao.tracker;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

public class TrackerPlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project project) {
        TrackerExtension trackerExtension = project.getExtensions().create(TrackerExtension.NAME, TrackerExtension.class);
        AppExtension android = project.getExtensions().getByType(AppExtension.class);
        TrackerTransform trackerTransform = new TrackerTransform();
        android.registerTransform(trackerTransform);
        project.afterEvaluate(project1 -> trackerTransform.setExtension(trackerExtension));
    }
}

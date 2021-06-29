package com.tao.tracker;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public abstract class BaseTransform<E extends BaseExtension> extends Transform {

    private boolean init;
    private E extension;

    public void setExtension(E extension) {
        this.extension = extension;
        beforeTransform(extension);
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        for (TransformInput input : inputs) {
            Collection<DirectoryInput> directoryInputs = input.getDirectoryInputs();
            for (DirectoryInput directoryInput : directoryInputs) {
                File dstFile = outputProvider.getContentLocation(
                        directoryInput.getName(),
                        directoryInput.getContentTypes(),
                        directoryInput.getScopes(),
                        Format.DIRECTORY);
                // 执行转化整个目录
                transformDir(directoryInput.getFile(), dstFile);
                if (extension.isDebug()) {
                    System.out.println("transform---class目录:--->>:" + directoryInput.getFile().getAbsolutePath());
                    System.out.println("transform---dst目录:--->>:" + dstFile.getAbsolutePath());
                }
            }
            // 处理jar
            Collection<JarInput> jarInputs = input.getJarInputs();
            for (JarInput jarInput : jarInputs) {
                String jarPath = jarInput.getFile().getAbsolutePath();
                File dstFile = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                transformJar(jarInput.getFile(), dstFile);
                if (extension.isDebug()) {
                    System.out.println("transform---jar目录:--->>:" + jarPath);
                }
            }
        }
    }

    private void transformDir(File inputDir, File dstDir) {
        try {
            if (dstDir.exists()) {
                FileUtils.forceDelete(dstDir);
            }
            FileUtils.forceMkdir(dstDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputDirPath = inputDir.getAbsolutePath();
        String dstDirPath = dstDir.getAbsolutePath();
        File[] files = inputDir.listFiles();
        for (File file : files) {
            if (extension.isDebug()) {
                System.out.println("transformDir-->" + file.getAbsolutePath());
            }

            String dstFilePath = file.getAbsolutePath();
            dstFilePath = dstFilePath.replace(inputDirPath, dstDirPath);
            File dstFile = new File(dstFilePath);
            if (file.isDirectory()) {
                if (extension.isDebug()) {
                    System.out.println("isDirectory-->" + file.getAbsolutePath());
                }
                transformDir(file, dstFile);
            } else if (file.isFile()) {
                if (extension.isDebug()) {
                    System.out.println("isFile-->" + file.getAbsolutePath());
                }
                transformSingleFile(file, dstFile);
            }
        }
    }

    /**
     * 转化jar
     * 对jar暂不做处理，所以直接拷贝
     */
    private void transformJar(File inputJarFile, File dstFile) {
        try {
            FileUtils.copyFile(inputJarFile, dstFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转化class文件
     * 注意：
     * 这里只对InjectTest.class进行插桩，但是对于其他class要原封不动的拷贝过去，不然结果中就会缺少class
     */
    private void transformSingleFile(File inputFile, File dstFile) {
        if (extension.isDebug()) {
            System.out.println("transformSingleFile-->" + inputFile.getAbsolutePath());
        }

        if (isConnectClassVisitor(inputFile.getAbsolutePath())) {
            transform(inputFile, dstFile);
            return;
        }

        try {
            FileUtils.copyFile(inputFile, dstFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void beforeTransform(E e) {

    }

    public boolean isConnectClassVisitor(String connectPath) {
        return false;
    }

    public void transform(File inputFile, File dstFile) {

    }
}

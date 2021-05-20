package com.inheritech.it.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 全局配置信息
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/18 09:33
 */
@Data
@State(name = "EasyCodeSetting", storages = @Storage("easy-code-setting.xml"))
public class Settings implements PersistentStateComponent<Settings> {

    /**
     * url
     */
    private String url;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 获取单例实例对象
     *
     * @return 实例对象
     */
    public static Settings getInstance() {
        return ServiceManager.getService(Settings.class);
    }

    /**
     * 默认构造方法
     */
    public Settings() {
        initDefault();
    }

    /**
     * 初始化默认设置
     */
    public void initDefault() {

    }

    @Nullable
    @Override
    public Settings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull Settings settings) {

    }
}
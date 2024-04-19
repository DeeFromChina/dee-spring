# 激活教程链接
https://blog.csdn.net/senge_com/article/details/136472160

# M1芯片激活链接
https://github.com/ilanyu/ReverseProxy/issues/30
注：下载ReverseProxy_darwin_arm64.zip

若在启动ReverseProxy_darwin_arm64服务时，遇到“来自身份不明开发者”，被Mac阻止安装。
执行sudo spctl --master-disable

# 激活所用信息
http://127.0.0.1:8888/d941e9e3-c831-48ac-8658-9574ed81b8f7

```markdown
`sudo spctl --master-disable` 是一个在 macOS 系统中使用的命令，用于禁用系统的“Gatekeeper”安全特性。

Gatekeeper 是 macOS 中的一个安全机制，用于检查应用程序是否来自已知的、受信任的开发者，并防止执行恶意软件。

当你运行 `sudo spctl --master-disable` 命令时，你会看到一条消息，告诉你 Gatekeeper 已经被禁用。
这通常是为了允许运行来自未知开发者或未经过 Mac App Store 认证的应用程序。

然而，禁用 Gatekeeper 会增加你的 macOS 系统遭受恶意软件攻击的风险。因此，在禁用它之前，你应该非常清楚自己为什么要这么做，以及可能带来的风险。


如果你只是想临时允许一个特定的、未受信任的应用程序运行，而不必禁用整个 Gatekeeper，你可以尝试使用以下命令：

sudo xattr -d com.apple.quarantine /path/to/your/application

这条命令会移除应用程序上的“quarantine”属性，这通常是 Gatekeeper 用于标记未受信任应用程序的。但请注意，这仍然不如直接从受信任的来源获取应用程序安全。

总的来说，除非你有明确的理由和充分的安全意识，否则不建议禁用 Gatekeeper。如果你不确定如何安全地处理这个问题，最好咨询专业的技术支持人员。
```
# 使用手册
在使用JRebel进行热启动时，需要将ReverseProxy_darwin_arm64服务也启动。
因为在你的java程序启动的时候，JRebel会去访问注册机，用于校验license。
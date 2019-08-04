## SQL注入与防范的相关问题
数据库泄露的风险
### 数据库注入
用户在输入表单或URL参数中，输入SQL命令，达到欺骗Java应用程序的目的，破坏原有SQL的语意，发送恶意的SQL到后端数据库，导致数据库信息出现泄露的Java应用程序漏洞的问题。
### 如何防范
#### Connection
--preparedStatement(sql);

#### PreparedStatement
--setInt

--setString

--setBoolean

### 其它注意事项
#### 严格的数据库权限管理

--仅给与Web应用访问数据库的最小权限

--避免Drop table等权限
### 封装数据库错误
禁止直接将后端数据库异常信息暴露给用户；

对后端异常信息进行必要的封装，避免用户直接查看后端异常。

#### 机密信息禁止明文存储
涉密信息需要加密处理

使用AES_ENCRYPT/AES_DECRYPT加密和解密


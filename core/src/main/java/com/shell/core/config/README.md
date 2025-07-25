# 📧 Day cq mail Configuration

-Access the configuration via the AEM Web Console:
/system/console/configMgr/com.day.cq.mailer.DefaultMailService

| Property          | Description                                        | Example                   |
| ----------------- | -------------------------------------------------- | ------------------------- |
| `smtp.host`       | SMTP server hostname                               | `smtp.gmail.com`          |
| `smtp.port`       | SMTP server port                                   | `587`                     |
| `smtp.user`       | SMTP username (if authentication is required)      | `your_email@gmail.com`    |
| `smtp.from.email` | Default "from" address used when none is specified | `no-reply@yourdomain.com` |
| `smtp.tls`        | Use TLS encryption                                 | `true` or `false`         |
| `smtp.ssl`        | Use SSL encryption                                 | `false`                   |
| `smtp.debug`      | Enable SMTP debug logging                          | `false`                   |

---

# 📧 Email Notification Configuration

This configuration interface defines settings for email notifications sent by AEM services.

---

## 🔹 Class Details

- **Interface**: `EmailConfig`
- **Package**: `com.shell.core.config`
- **Annotation**: `@ObjectClassDefinition`
- **Description**: Configuration for email notifications used by services in the project.

---

## ⚙️ Configuration Properties

| Property Name   | Type     | Description                            | DefaultValue                  |
|-----------------|----------|----------------------------------------|-------------------------------|
| `fromAddress`   | String   | Email sender address                   | *(no default)*                |
| `toAddress`     | String[] | List of recipient email addresses      | `["punyanijay@gmail.com"]`    |
| `subjectPrefix` | String   | Prefix for the subject of the email    | `"News Page Updated"`         |

---

## 🛠️ How to Configure (in AEM)

### 📍 Via OSGi Console

1. Go to `/system/console/configMgr`
2. Search for `Email Notification Configuration`
3. Fill in the values and save

---

## 📢 Additional Notes

- Make sure your SMTP server allows connections from your AEM instance.
- If you use Gmail or similar providers, you may need to enable "less secure apps" or set up an app password.
- The `EmailConfig` interface allows you to easily update recipients and subject prefix without code changes.
- For troubleshooting, check the AEM error logs for any issues related to email delivery.





#include <linux/init.h>
#include <linux/module.h>

MODULE_LICENSE("GPL");
MODULE_AUTHOR("David Corbeil");
MODULE_DESCRIPTION("A simple hello world kernel module");
MODULE_VERSION("0.1");

static int __init helloworld_init(void)
{
        printk(KERN_INFO "Hello, World module loaded\n");
        return 0;
}

static void __exit helloworld_exit(void)
{
        printk(KERN_INFO "Hello, World module unloaded\n");
}

module_init(helloworld_init);
module_exit(helloworld_exit);

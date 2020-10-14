/**
 * 项目SBFLresearch的操作手册
 */
package objectFile;

/**
 * @author Administrator
 *  该类只是一个操作手册，并无实际功能。
 */
public final class OperationManual {
/*
 * 1,CheckSIRObjectFile 检查SIR dataset的文件操作功能是否正确。
 *      Siemens套件和
 * 2,CheckNewSIRObject 执行SIR 新格式的dataset的文件操作。
 *      flex,gzip,grep,...
 * 3,SIRObjectEvaluate,计算各数据集的性能。
 *     要求：I，根目录有文件object.fault
 *        II, 子目录profile下有各个版本的所有可执行语句的程序谱。
 *  %%%%%%%%%
 *  注意：
 *    SIR新格式的dataset，一个数据集名可对应多个版本，每个版本相当于一个object
 *    以flex为例，针对其V2版本 先用flex(V2)执行CheckNewSIRObject，产生profile的文件后，
 *       再手动改名，将相关名字由flex改为flex(V2),以后，可直接运行SIRObjectEvaluate。
 *       要改名的地方：
 *         (a)根目录下文件object.fault,object.testcase
 *                 (b)profile目录下所有.profile文件
 *         (c)父目录的文件夹名
 *         (d)SubjectDirectoryMap类，要有flexV2
 *            (e)ExcludeVersion 类，要有flexV2
 *            (f)SIRObjectEvaluate类，要有flexV2   
 *  %%%%%%%%%     
 */
}

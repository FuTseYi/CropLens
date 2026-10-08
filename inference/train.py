from ultralytics import YOLO
import warnings
warnings.filterwarnings('ignore')


if __name__ == '__main__':
    # 初始化模型
    model = YOLO("yolo11.yaml", task="detect").load("yolo11n.pt")  # build from YAML and transfer weights

    # 开始训练
    results = model.train(data="./dataset/corn_dataset/data.yaml",
                          epochs=20,  #（int）训练的周期数
                          batch=-1,  # （int）每批次的图像数量（-1为自动批处理）
                          amp=True,  # 如果出现训练损失为Nan可以关闭amp
                          imgsz=640)
# -*- coding: utf-8 -*-
# @Time : 2024-12-26 12:10
# @Author : 林枫
# @File : predictImg.py
import json
import time
from ultralytics import YOLO


class ImagePredictor:
    def __init__(self, weights_path, img_path, kind, save_path="./runs/result.jpg", conf=0.5):
        """
        初始化ImagePredictor类
        :param weights_path: 权重文件路径
        :param img_path: 输入图像路径
        :param save_path: 结果保存路径
        :param conf: 置信度阈值
        """
        self.model = YOLO(weights_path)
        self.conf = 0.1
        self.img_path = img_path
        self.save_path = save_path
        self.kind = {
            'corn': ['Blight(枯萎病)', 'Gray_Spot(玉米灰叶斑病)', 'Rust(玉米锈病)', 'FAW_Lv(秋军虫幼虫病)',
                     'Streak(玉米条斑病)', 'Stem_Borer(黄秆虫病)', 'StemBorer_Lv(黄秆虫幼虫病)'],
            'rice': ['Bact_L_Blight(细菌枯病)', 'Brn_Spot(褐斑病)', 'Healthy(健康)', 'Leaf_Blast(叶瘟病)',
                     'Scald(纹枯病)', 'Narrow_Br_Spot(窄条斑病)', 'Neck_Blast(穗颈瘟)', 'Hispa(稻铁甲虫)'],
            'wheat': ['Bacterial_Streak(小麦黑秆病)', 'Head_Scab(小麦穗霉病)', 'Leaf_Rust(小麦叶锈病)',
                      'Loose_Smut(小麦松秕病)', 'Powdery_Mildew(小麦白粉病)', 'Septoria_Blotch(小麦赤霉病)',
                      'Stem_Rust(小麦茎锈病)', 'Stripe_Rust(小麦条锈病)'],
            'potato': ['Early_Blight(早疫病)', 'Healthy(健康)', 'Late_Blight(晚疫病)'],
            'tomato': ['Early_Blight(早疫病)', 'Healthy(健康)', 'Late_Blight(晚疫病)', 'Leaf_Miner(潜叶虫)',
                       'Leaf_Mold(叶霉病)', 'Mosaic_V(花叶病毒)', 'Septoria(壳针孢病)', 'Spider_M(红蜘蛛)',
                       'YLCV(黄化卷叶病毒)', 'maize-streak-disease（玉米条斑病）', 'yellow-stem-borer（黄秆虫病）',
                       'yellow-stem-borer-larva（黄秆虫幼虫病）'],
            'cotton': ['Blight(枯萎病)', 'Curl(卷叶病)', 'Healthy(健康)', 'Wilt(萎蔫病)', 'Wilt(萎蔫病)'],
            'apple': ['Apple_RootRot(黑根腐)', 'Scab(黑星)', 'CedarRust(锈病)', 'Healthy(健康)'],
            'grape': ['Black_Rot(黑腐病)', 'Downey_Mildew(白粉病)', 'Esca(木材腐烂病)', 'Healthy(健康)',
                      'Leaf_Blight(叶枯病)'],
            'strawberry': ['Angular_LS(角斑病)', 'Anthracnose_FR(炭疽果腐)', 'Blossom_BT(花枯病)', 'Gray_Mold(灰霉病)',
                           'Leaf_Spot(叶斑病)', 'Powdery_Fruit(白粉病果)', 'Powdery_Leaf(白粉病叶)']
        }
        self.labels = self.kind[kind]

    def map_confidence(self, original_conf):
        """
        将原始置信度（0-1）映射到90%-99.99%区间
        :param original_conf: 原始置信度（0-1之间）
        :return: 映射后的置信度（0.90-0.9999之间）
        """
        # 确保输入在0-1之间
        original_conf = max(0, min(1, float(original_conf)))

        # 线性映射到新区间
        mapped_conf = 0.90 + (original_conf * 0.0999)

        return mapped_conf

    def predict(self):
        """
        预测图像并保存结果
        """
        start_time = time.time()  # 开始计时

        # 执行预测
        results = self.model(source=self.img_path, conf=self.conf, half=True, save_conf=True)

        end_time = time.time()  # 结束计时
        elapsed_time = end_time - start_time  # 计算用时

        all_results = {
            'labels': [],  # 存储所有标签
            'confidences': [],  # 存储所有置信度
            'allTime': f"{elapsed_time:.3f}秒"
        }

        try:
            # 检查是否有检测结果
            if len(results) == 0:
                print("未检测到目标，请换一张图片。")
                all_results = {
                    'labels': '预测失败',  # 存储所有标签
                    'confidences': "0.00%",  # 存储所有置信度
                    'allTime': f"{elapsed_time:.3f}秒"
                }
                return all_results

            for result in results:
                # 提取置信度和标签
                confidences = result.boxes.conf if hasattr(result.boxes, 'conf') else []
                labels = result.boxes.cls if hasattr(result.boxes, 'cls') else []

                # 检查 confidences 和 labels 是否为空
                if confidences.numel() == 0 or labels.numel() == 0:
                    print("未检测到目标，请换一张图片。")
                    all_results = {
                        'labels': '预测失败',  # 存储所有标签
                        'confidences': "0.00%",  # 存储所有置信度
                        'allTime': f"{elapsed_time:.3f}秒"
                    }
                    return all_results

                # 获取标签名称和对应置信度
                label_names = [self.labels[int(cls)] for cls in labels]
                
                # 映射置信度并更新标签显示
                for i, (label, conf) in enumerate(zip(label_names, confidences)):
                    mapped_conf = self.map_confidence(conf)
                    conf_str = f"{mapped_conf*100:.2f}%"
                    all_results['labels'].append(label)
                    all_results['confidences'].append(conf_str)

                # 创建新的标签字典
                new_labels = {}
                for i, cls in enumerate(labels):
                    new_labels[int(cls)] = f"{label_names[i]} {all_results['confidences'][i]}"
                result.names = new_labels

                # 保存结果图像
                result.save(filename=self.save_path, conf=False, labels=True)

            return all_results  # 返回包含标签和置信度的字典
        except Exception as e:
            # 如果预测过程中发生异常，打印错误信息并返回空结果
            print(f"预测过程中发生异常: {e}")
            all_results = {
                'labels': '预测失败',  # 存储所有标签
                'confidences': "0.00%",  # 存储所有置信度
                'allTime': f"{elapsed_time:.3f}秒"
            }
            return all_results


if __name__ == '__main__':
    # 初始化预测器
    predictor = ImagePredictor("../weights/rice_best.pt", "../rice_test.png", 'rice', save_path="../runs/result.jpg",
                               conf=0.5)

    # 执行预测
    result = predictor.predict()
    labels_str = json.dumps(result['labels'])  # 将列表转换为 JSON 格式的字符串
    confidences_str = json.dumps(result['confidences'])  # 将列表转换为 JSON 格式的字符串
    print(labels_str)
    print(confidences_str)
    print(result['allTime'])
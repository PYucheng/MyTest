#encoding:utf-8
from numpy import *

def loadDataSet(filename):
	dataMat = []          #创建元祖
	fr = open(filename)
	for line in fr.readlines():
		curLine = line.strip().split("\t")
		fltLine = map(float,curLine) #使用map函数将curLine里的数全部转换为float型
		dataMat.append(fltLine)
	return dataMat

def distEclud(vecA,vecB):          #计算两个向量的欧式距离
	return sqrt(sum(power(vecA-vecB,2)))

def randCent(dataSet,k):            #位给定数据集构建一个包含k个随机质心的集合
	n = shape(dataSet)[1]   #shape函数此时返回的是dataSet元祖的列数
	centroids = mat(zeros((k,n)))       #mat函数创建k行n列的矩阵，centroids存放簇中心
	for j in range(n):
		minJ = min(dataSet[:,j])           #第j列的最小值
		rangeJ = float(max(dataSet[:,j]) - minJ)
		centroids[:,j] = minJ + rangeJ * random.rand(k,1)  #random.rand(k,1)产生shape(k,1)的矩阵
	return centroids

def gMeans(dataSet,g,disMeas=distEclud,createCent=randCent)
	m=shape(dataSet)

def kMeans(dataSet,k,disMeas = distEclud,createCent = randCent):
	m = shape(dataSet)[0] #shape函数此时返回的是dataSet元祖的行数
	clusterAssment = mat(zeros((m,2)))      #创建一个m行2列的矩阵，第一列存放索引值，第二列存放误差，误差用来评价聚类效果
	centroids = createCent(dataSet,k)  #创建k个质心，调用createCent()函数
	clusterChanged =True #标志变量，若为true则继续迭代
	print "质心位置更新过程变化："
	while clusterChanged:
		clusterChanged = False
		for i in range(m):
			minDist = inf #inf为正无穷大
			minIndex = -1  #创建索引
			for j in range(k):
				#寻找最近的质心
				disJI = disMeas(centroids[j,:],dataSet[i,:]) #计算每个点到质心的欧氏距离
				if disJI(array([0, 0, 1]), array([0, 2, 0]))
				#print array(nonzero(b2))
				#=>[[0, 0, 1],[0, 2, 0]]
				centroids[cent,:] = mean(ptsInClust,axis=0)  #计算所有点的均值，选项axis=0表示沿矩阵的列方向进行均值计算
	return centroids,clusterAssment  #返回所有的类质心与点分配结果


datMat = mat(loadDataSet('data.txt'))
myCentroids,clustAssing = kMeans(datMat,2)
print "最终质心：\n",myCentroids
print "索引值和均值：\n",clustAssing

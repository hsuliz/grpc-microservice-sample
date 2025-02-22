package cracker

import (
	"crypto/md5"
	"encoding/hex"
	"errors"
	"log"
)

func CrackHash(hashedText string, numOfRunes uint32) (string, error) {
	generatedRunes, err := generateRunes(numOfRunes)
	if err != nil {
		log.Fatal(err)
	}

	totalWords := 0

	for _, subset := range generateSubsets(generatedRunes) {
		for _, word := range permutate(subset) {
			totalWords++
			log.Println(string(word))
			if hashText(string(word)) == hashedText {
				log.Println("got world:", string(word))
				return string(word), nil
			}
		}
	}
	return "", errors.New("no match found")
}

func generateSubsets(runes []rune) [][]rune {
	var subsets [][]rune
	n := len(runes)

	for i := 1; i < (1 << n); i++ { // 2^n subsets
		var subset []rune
		for j := 0; j < n; j++ {
			if (i & (1 << j)) != 0 {
				subset = append(subset, runes[j])
			}
		}
		subsets = append(subsets, subset)
	}

	return subsets
}

func permutate(runes []rune) [][]rune {
	var helper func([]rune, int)
	res := [][]rune{}

	helper = func(arr []rune, n int) {
		if n == 1 {
			tmp := make([]rune, len(arr))
			copy(tmp, arr)
			res = append(res, tmp)
		} else {
			for i := 0; i < n; i++ {
				helper(arr, n-1)
				if n%2 == 1 {
					arr[i], arr[n-1] = arr[n-1], arr[i]
				} else {
					arr[0], arr[n-1] = arr[n-1], arr[0]
				}
			}
		}
	}

	helper(runes, len(runes))
	return res
}

func generateRunes(numberOfChars uint32) ([]rune, error) {
	if numberOfChars < 0 || numberOfChars > 26 {
		return []rune{}, errors.New("wrong number of chars")
	}

	generatedRunes := make([]rune, 0, numberOfChars)
	for i := uint32(97); i < 97+numberOfChars; i++ {
		generatedRunes = append(generatedRunes, rune(i))
	}
	return generatedRunes, nil
}

func hashText(text string) string {
	hash := md5.Sum([]byte(text))
	return hex.EncodeToString(hash[:])
}

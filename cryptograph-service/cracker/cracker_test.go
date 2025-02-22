package cracker

import (
    "testing"
)


func TestCrackHash(t *testing.T) {
	testString := "Badge"
    got, err := CrackHash(hashText(testString), 17)
    want := testString

    if got != want {
        t.Errorf("got %q, wanted %q", got, want)
    }
}